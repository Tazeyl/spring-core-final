package sokolov.spring.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import sokolov.spring.memory.Memory;
import sokolov.spring.model.User;

import java.util.List;
import java.util.Objects;

@Component
public class UserService {

    private final Memory memory;
    private final AccountService accountService;

    public UserService(Memory memory, @Lazy AccountService accountService) {
        this.memory = memory;
        this.accountService = accountService;
    }

    public User create(String login) {
        if (memory.getUsers().stream().anyMatch(user -> Objects.equals(user.getLogin(), login))) {
            throw new IllegalArgumentException(String.format("Exists user with login = %s", login));
        }

        User user = new User(memory.getNextIdUser(), login);
        memory.getUsers().add(user);
        accountService.create(user.getId());

        return user;
    }

    public List<User> getAllUsers() {
        return memory.getUsers();
    }

    public User getUser(Long id) {
        return memory.getUsers().stream().filter(user1 -> Objects.equals(user1.getId(), id)).findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Not found user by id = %s", id)));
    }
}
