package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.repository.menu.MenuRepositoryImpl;

import static ru.restaurantVoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {

    private final MenuRepositoryImpl repository;

    @Autowired
    public MenuService(MenuRepositoryImpl repository) {
        this.repository = repository;
    }

    public Menu create(Menu menu, int restaurant_id) {
        Assert.notNull(menu, "menu must not be null");
        return repository.save(menu, restaurant_id);
    }

    public void update(Menu menu, int restaurant_id) {
        Assert.notNull(menu, "menu must not be null");
        checkNotFoundWithId(repository.save(menu, restaurant_id), menu.getId());
    }

    public void delete(int id, int restaurant_id) {
        checkNotFoundWithId(repository.delete(id, restaurant_id), id);
    }
}