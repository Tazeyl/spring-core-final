package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.model.User;
import sokolov.spring.service.UserService;

import java.util.List;

@Component
public class ShowAllUsersCommand implements OperationCommand {

    private final UserService userService;

    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("List of all users:");
        List<User> users = userService.getAllUsers();
        System.out.println("Users: " + users);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
