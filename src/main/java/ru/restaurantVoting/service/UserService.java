package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.User;
import ru.restaurantVoting.repository.UserRepository;

import static ru.restaurantVoting.util.ValidationUtil.checkNotFound;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public User getByEmail(String email) {
        String lowerCaseEmail = email.toLowerCase();
        Assert.notNull(lowerCaseEmail, "email must not be null");
        return checkNotFound(repository.getByEmail(lowerCaseEmail), "email=" + email);
    }
}