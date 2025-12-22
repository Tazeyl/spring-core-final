package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;

@Component
public class AccountCloseCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;

    public AccountCloseCommand(CommonScanner commonScanner, AccountService accountService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Enter account ID to close:\n");
        Long accountId = commonScanner.getLong("accountId");
        accountService.close(accountId);
        System.out.println("Account with ID= " + accountId + " has been closed. ");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
