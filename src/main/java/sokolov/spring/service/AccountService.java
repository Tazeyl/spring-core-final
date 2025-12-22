package sokolov.spring.service;

import org.springframework.stereotype.Component;
import sokolov.spring.configuration.AccountProperties;
import sokolov.spring.memory.Memory;
import sokolov.spring.model.Account;
import sokolov.spring.model.User;

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
                .orElseThrow(() -> new IllegalArgumentException("Do not close first Account"));
        transfer(account.getId(), firstAccount.getId(), account.getMoneyAmount());
        User user = userService.getUser(account.getUserId());
        user.getAccounts().remove(account);
        memory.getAccounts().remove(account);

    }

    public void deposit(Long accountId, Integer money) {
        Account account = getAccount(accountId);
        if (money <= 0) {
            throw new IllegalArgumentException("Money must be greater than 0");
        }
        Integer amount = account.getMoneyAmount();
        account.setMoneyAmount(amount + money);

    }

    public void transfer(Long senderAccountId, Long recipientAccountId, Integer money) {
        Account senderAccount = getAccount(senderAccountId);
        Account recipientAccount = getAccount(recipientAccountId);

        int commission = 0;
        if (!Objects.equals(senderAccount.getUserId(), recipientAccount.getUserId())) {
            // округление + потеря данных
            commission = (int)( money * accountProperties.getTransferCommission() / 100);
        }

        withdraw(senderAccountId, money);
        deposit(recipientAccountId, money - commission);

    }

    public void withdraw(Long accountId, Integer money) {
        Account account = getAccount(accountId);
        if (money <= 0) {
            throw new IllegalArgumentException("money must be greater than 0");
        }
        Integer amount = account.getMoneyAmount();
        if (money > amount) {
            throw new IllegalArgumentException("money must be less then amount an current account ");
        }
        account.setMoneyAmount(amount - money);
    }

    private Account getAccount(Long accountId) {
        return memory.getAccounts().stream().filter(account1 -> Objects.equals(account1.getId(), accountId)).findFirst().orElseThrow(() -> new IllegalArgumentException("not found account with id = " + accountId));
    }
}
