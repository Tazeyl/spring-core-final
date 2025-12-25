package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.model.Account;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;

@Component
public class AccountCreateCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;

    public AccountCreateCommand(CommonScanner commonScanner, AccountService accountService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Enter the user id for which to create an account:");
        Long userId = commonScanner.getLong("user id");
        Account account = accountService.create(userId);
        System.out.println("New account created with ID: " + account.getId() + " for user: " + account.getUser().getLogin());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
