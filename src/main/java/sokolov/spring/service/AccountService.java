package sokolov.spring.service;

import org.springframework.stereotype.Component;
import sokolov.spring.configuration.AccountProperties;
import sokolov.spring.model.Account;
import sokolov.spring.model.User;
import sokolov.spring.utils.TransactionHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class AccountService {
    private final AccountProperties accountProperties;
    private final UserService userService;
    private final TransactionHelper transactionHelper;


    public AccountService(AccountProperties accountProperties, UserService userService, TransactionHelper transactionHelper) {
        this.accountProperties = accountProperties;
        this.userService = userService;
        this.transactionHelper = transactionHelper;
    }

    public Account create(Long userId) {
        return transactionHelper.executeInTransaction(session -> {
            User user = userService.getUser(userId);
            Account account = new Account(user, accountProperties.getDefaultAmount());
            session.persist(account);
            return account;
        });
    }

    public void close(Long accountId) {
        transactionHelper.executeInTransaction(session -> {
            Account account = getAccount(accountId);
            Long userId = account.getUser().getId();
            List<Account> accounts = getAccountsByUserId(userId);
            Account firstAccount = accounts.stream()
                    .filter(account1 -> !Objects.equals(account1.getId(), accountId))
                    .min((o1, o2) -> (int) (o1.getId() - o2.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Do not close single Account"));
            transfer(account.getId(), firstAccount.getId(), account.getMoneyAmount());
            session.remove(account);
            return null;
        });

    }

    public void deposit(Long accountId, BigDecimal money) {
        transactionHelper.executeInTransaction(session -> {
            if (money.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Money must be greater than 0");
            }
            Account account = getAccount(accountId);
            BigDecimal amount = account.getMoneyAmount();
            BigDecimal sum = amount.add(money);
            account.setMoneyAmount(sum);
            return null;
        });


    }

    public void transfer(Long senderAccountId, Long recipientAccountId, BigDecimal money) {
        Account senderAccount = getAccount(senderAccountId);
        Account recipientAccount = getAccount(recipientAccountId);
        BigDecimal commission = BigDecimal.ZERO;
        if (!Objects.equals(senderAccount.getUser(), recipientAccount.getUser())) {
            // округление + потеря данных
            commission = calculateDiscount(money, accountProperties.getTransferCommission());
        }
        withdraw(senderAccountId, money);
        BigDecimal depositedWithoutCommission = money.subtract(commission);

        deposit(recipientAccountId, depositedWithoutCommission);

    }

    public void withdraw(Long accountId, BigDecimal money) {
        transactionHelper.executeInTransaction(session -> {
            if (money.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("money must be greater than 0");
            }
            Account account = getAccount(accountId);
            BigDecimal amount = account.getMoneyAmount();
            if (money.compareTo(amount) > 0) {
                throw new IllegalArgumentException("money must be less then amount an current account ");
            }
            BigDecimal newAmount = amount.subtract(money);
            account.setMoneyAmount(newAmount);
            return null;
        });

    }

    private Account getAccount(Long accountId) {
        return transactionHelper.executeInTransaction(session -> {
            Account account = session.find(Account.class, accountId);
            if (account == null) {
                throw new IllegalArgumentException(String.format("not found account with id = %s", accountId));
            }
            return account;
        });

    }

    private BigDecimal calculateDiscount(BigDecimal original,
                                         BigDecimal percent) {
        BigDecimal percentDecimal = percent
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_EVEN);
        return original.multiply(percentDecimal)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private List<Account> getAccountsByUserId(Long userId) {
        return transactionHelper.executeInTransaction(session ->
                session.createQuery("select a from Account a where a.user.id = :userId", Account.class)
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }
}
