package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.Vote;
import ru.restaurantVoting.repository.vote.VoteRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurantVoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final VoteRepositoryImpl repository;

    @Autowired
    public VoteService(VoteRepositoryImpl repository) {
        this.repository = repository;
    }

    public Vote create(Vote vote, int userId, int menuId) {
        Assert.notNull(vote, "vote must not be null");
        return repository.save(vote, userId, menuId);
    }

    public void update(Vote vote, int userId, int menuId) {
        Assert.notNull(vote, "vote must not be null");
        checkNotFoundWithId(repository.save(vote, userId, menuId), vote.getId());
    }

    public Vote get(int id, int userId, int menuId) {
        return checkNotFoundWithId(repository.get(id, userId, menuId), id);
    }

    public Vote getForUserAndDate(int userId, LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return checkNotFoundWithId(repository.getForUserAndDate(userId, date), userId);
    }

    public List<Vote> getAll() {
        return repository.getAll();
    }

    public List<Vote> getAllByDate(LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return repository.getAllByDate(date);
    }
}