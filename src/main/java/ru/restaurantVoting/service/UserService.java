package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.User;
import ru.restaurantVoting.repository.UserRepository;
import ru.restaurantVoting.web.AuthorizedUser;

import java.util.List;

import static ru.restaurantVoting.util.ValidationUtil.checkNotFound;
import static ru.restaurantVoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Transactional
    public void enable(int id, boolean enable) {
        User user = get(id);
        user.setEnabled(enable);
        repository.save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}