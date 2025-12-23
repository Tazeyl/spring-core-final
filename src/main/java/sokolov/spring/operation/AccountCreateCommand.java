package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.model.Account;
import sokolov.spring.model.User;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;
import sokolov.spring.service.UserService;

@Component
public class AccountCreateCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;
    private final UserService userService;

    public AccountCreateCommand(CommonScanner commonScanner, AccountService accountService, UserService userService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("Enter the user id for which to create an account:");
        Long userId = commonScanner.getLong("user id");
        Account account = accountService.create(userId);
        User user = userService.getUser(account.getUserId());
        System.out.println("New account created with ID: " + account.getId() + " for user: " + user.getLogin());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
