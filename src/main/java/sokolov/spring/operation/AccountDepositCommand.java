package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;

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
        System.out.println("Enter account ID:\n");
        Long accountId = commonScanner.getLong("accountId");
        System.out.println("Enter amount to deposit:\n\n");
        Integer money = commonScanner.getInteger("accountId");
        accountService.deposit(accountId, money);
        System.out.println("Amount " + money + " deposited to account ID: " + accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
