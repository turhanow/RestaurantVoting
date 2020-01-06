package ru.restaurantVoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.repository.menu.MenuRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static ru.restaurantVoting.util.ValidationUtil.checkNotFound;
import static ru.restaurantVoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {

    private final MenuRepositoryImpl repository;

    @Autowired
    public MenuService(MenuRepositoryImpl repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "menusByDate", allEntries = true)
    public Menu create(Menu menu, int restaurant_id) {
        Assert.notNull(menu, "menu must not be null");
        return repository.save(menu, restaurant_id);
    }

    @CacheEvict(value = "menusByDate", allEntries = true)
    public void update(Menu menu, int restaurant_id) {
        Assert.notNull(menu, "menu must not be null");
        checkNotFoundWithId(repository.save(menu, restaurant_id), menu.getId());
    }

    public Menu get(int id, int restaurant_id) {
        return checkNotFoundWithId(repository.get(id, restaurant_id), id);
    }

    public Menu findByRestaurantAndDate(String name, LocalDate date) {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(date, "date must not be null");
        return checkNotFound(repository.findByRestaurantAndDate(name, date), "name=" + name + "and date=" + date);
    }

    public Menu findById(int id) {
        return checkNotFoundWithId(repository.findById(id), id);
    }

    @Cacheable("menusByDate")
    public List<Menu> findByDate(LocalDate date) {
        if (Objects.isNull(date)) {
            date = LocalDate.now();
        }
        return repository.findByDate(date);
    }

    public List<Menu> findByRestaurant(String name) {
        Assert.notNull(name, "name must not be null");
        return repository.findByRestaurant(name);
    }

    @CacheEvict(value = "menusByDate", allEntries = true)
    public void delete(int id, int restaurant_id) {
        checkNotFoundWithId(repository.delete(id, restaurant_id), id);
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }
}