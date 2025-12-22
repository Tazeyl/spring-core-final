package sokolov.spring.service;

import org.springframework.stereotype.Component;
import sokolov.spring.configuration.AccountProperties;
import sokolov.spring.memory.Memory;
import sokolov.spring.model.Account;
import sokolov.spring.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class AccountService {

    private final Memory memory;

    private final AccountProperties accountProperties;
    private final UserService userService;

    public AccountService(Memory memory, AccountProperties accountProperties, UserService userService) {
        this.memory = memory;
        this.accountProperties = accountProperties;
        this.userService = userService;
    }

    public Account create(Long userId) {
        User user = userService.getUser(userId);
        Account account = new Account(memory.getNextIdAccount(), userId, accountProperties.getDefaultAmount());
        user.getAccounts().add(account);
        memory.getAccounts().add(account);
        return account;
    }

    public void close(Long accountId) {
        Account account = getAccount(accountId);
        Account firstAccount = memory.getAccounts().stream()
                .filter(account1 -> !Objects.equals(account1.getId(), accountId))
                .filter(account1 -> Objects.equals(account1.getUserId(), account.getUserId()))
                .min((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Do not close single Account"));
        transfer(account.getId(), firstAccount.getId(), account.getMoneyAmount());
        User user = userService.getUser(account.getUserId());
        user.getAccounts().remove(account);
        memory.getAccounts().remove(account);

    }

    public void deposit(Long accountId, BigDecimal money) {
        Account account = getAccount(accountId);
        if (money.compareTo(BigDecimal.ZERO) <0) {
            throw new IllegalArgumentException("Money must be greater than 0");
        }
        BigDecimal amount = account.getMoneyAmount();
        BigDecimal sum = amount.add(money);
        account.setMoneyAmount(sum);

    }

    public void transfer(Long senderAccountId, Long recipientAccountId, BigDecimal money) {
        Account senderAccount = getAccount(senderAccountId);
        Account recipientAccount = getAccount(recipientAccountId);

        BigDecimal commission = BigDecimal.ZERO;
        if (!Objects.equals(senderAccount.getUserId(), recipientAccount.getUserId())) {
            // округление + потеря данных

            commission = calculateDiscount(money, accountProperties.getTransferCommission());
        }

        withdraw(senderAccountId, money);
        BigDecimal depositedWithoutCommission = money.subtract(commission);

        deposit(recipientAccountId, depositedWithoutCommission);

    }

    public void withdraw(Long accountId, BigDecimal money) {
        Account account = getAccount(accountId);
        if (money.compareTo(BigDecimal.ZERO) <0) {
            throw new IllegalArgumentException("money must be greater than 0");
        }
        BigDecimal amount = account.getMoneyAmount();
        if (money.compareTo( amount)>0) {
            throw new IllegalArgumentException("money must be less then amount an current account ");
        }
        BigDecimal newAmount = amount.subtract(money);
        account.setMoneyAmount(newAmount);
    }

    private Account getAccount(Long accountId) {
        List<Account> account = memory.getAccounts().stream().filter(account1 -> Objects.equals(account1.getId(), accountId)).toList();
        if (account.size()> 1){
            throw new IllegalArgumentException(String.format("found more than 1 account with id = %s", accountId) );
        }
        if (account.isEmpty()) {
                throw  new IllegalArgumentException(String.format("not found account with id = %s",  accountId));
        }
        return account.get(0);
    }

    private BigDecimal calculateDiscount(BigDecimal original,
                                        BigDecimal percent) {
        BigDecimal percentDecimal = percent
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_EVEN);
        return original.multiply(percentDecimal)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
