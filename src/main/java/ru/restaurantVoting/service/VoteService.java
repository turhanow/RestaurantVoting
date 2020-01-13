package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.model.Vote;
import ru.restaurantVoting.repository.vote.VoteRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurantVoting.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepositoryImpl repository;
    private final MenuService menuService;

    @Autowired
    public VoteService(VoteRepositoryImpl repository, MenuService menuService) {
        this.repository = repository;
        this.menuService = menuService;
    }

    public Vote create(LocalDate date, int userId, int menuId) {
        Assert.notNull(date, "date must not be null");
        Menu menu = menuService.findById(menuId);
        checkExpiredDate(menu.getDate(), menuId);
        return repository.save(new Vote(null, date), userId, menuId);
    }

    public void update(Vote vote, int userId, int menuId) {
        Assert.notNull(vote, "vote must not be null");
        Menu menu = menuService.findById(menuId);
        checkExpiredDateWithTime(menu.getDate(), menuId);
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