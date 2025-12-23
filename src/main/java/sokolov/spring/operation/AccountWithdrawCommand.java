package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;
import sokolov.spring.utils.MoneyUtils;

import java.math.BigDecimal;

@Component
public class AccountWithdrawCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;

    public AccountWithdrawCommand(CommonScanner commonScanner, AccountService accountService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Enter account ID to withdraw from:");
        Long accountId = commonScanner.getLong("accountId");
        System.out.println("Enter amount to withdraw:");
        BigDecimal money = MoneyUtils.safeAmount(commonScanner.getString("amount"));
        accountService.withdraw(accountId, money);
        System.out.println("Amount " + money + " withdraw from account ID: " + accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
