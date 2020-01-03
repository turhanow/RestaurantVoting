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

    public Menu findById(int id) {
        return checkNotFoundWithId(repository.findById(id), id);
    }

    @Cacheable("menusByDate")
    public List<Menu> findByDate(LocalDate date) {
        Assert.notNull(date, "date must not be null");
        List<Menu> menuList = repository.findByDate(date);
        checkNotFound(!menuList.isEmpty(), date.toString());
        return menuList;
    }

    public List<Menu> findByRestaurant(int restaurant_id) {
        List<Menu> menuList = repository.findByRestaurant(restaurant_id);
        checkNotFoundWithId(!menuList.isEmpty(), restaurant_id);
        return menuList;
    }

    @CacheEvict(value = "menusByDate", allEntries = true)
    public void delete(int id, int restaurant_id) {
        checkNotFoundWithId(repository.delete(id, restaurant_id), id);
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }
}