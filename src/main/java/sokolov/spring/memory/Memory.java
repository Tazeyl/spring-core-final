package sokolov.spring.memory;

import org.springframework.stereotype.Component;
import sokolov.spring.model.Account;
import sokolov.spring.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class Memory {

    private List<User> users = new ArrayList<>();

    private List<Account> accounts = new ArrayList<>();

    private Long idUser = 0L;
    private Long idAccount = 0L;

    public List<User> getUsers() {
        return users;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Long getNextIdUser() {
        return ++idUser;
    }

    public Long getNextIdAccount() {
        return ++idAccount;
    }
}
