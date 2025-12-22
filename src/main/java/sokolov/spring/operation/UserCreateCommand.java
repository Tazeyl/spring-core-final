package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.model.User;
import sokolov.spring.utils.CommonScanner;
import sokolov.spring.service.UserService;

@Component
public class UserCreateCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final UserService userService;

    public UserCreateCommand(CommonScanner commonScanner, UserService userService) {
        this.commonScanner = commonScanner;
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("Enter login for new user:");
        String login = commonScanner.getString("login");
        User user = userService.create(login);
        System.out.println("User created: " + user.toString());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
