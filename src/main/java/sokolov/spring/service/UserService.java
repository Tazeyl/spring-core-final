package sokolov.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import sokolov.spring.model.User;
import sokolov.spring.utils.TransactionHelper;

import java.util.List;

@Component
public class UserService {

    private final AccountService accountService;
    private final TransactionHelper transactionHelper;

    public UserService(@Lazy AccountService accountService, TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
        this.accountService = accountService;
    }

    public User create(String login) {
        if (!getUserByLogin(login).isEmpty()) {
            throw new IllegalArgumentException(String.format("Exists user with login = %s", login));
        }

        User user = new User(login);
        user = save(user);
        accountService.create(user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        return transactionHelper.executeInTransaction(session -> {
            return session.createQuery("SELECT u from User u" +
                    " left join fetch u.accounts", User.class).getResultList();
        });
    }

    public User getUser(Long id) {
        User user = transactionHelper.executeInTransaction(session -> {
            return session.find(User.class, id);
        });

        if (user == null) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
        }
        return user;

    }

    private User save(User user) {
        return transactionHelper.executeInTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    private List<User> getUserByLogin(String login) {
        return transactionHelper.executeInTransaction(session -> {
            return session.createQuery("select u from User u where u.login = :login", User.class)
                    .setParameter("login", login).getResultList();
        });

    }
}
