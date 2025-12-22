package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;
import sokolov.spring.utils.MoneyUtils;

import java.math.BigDecimal;

@Component
public class AccountDepositCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;

    public AccountDepositCommand(CommonScanner commonScanner, AccountService accountService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Enter account ID:");
        Long accountId = commonScanner.getLong("accountId");
        System.out.println("Enter amount to deposit:");
        BigDecimal amount = MoneyUtils.safeAmount(commonScanner.getString("amount"));
        accountService.deposit(accountId, amount);
        System.out.println("Amount " + amount + " deposited to account ID: " + accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
