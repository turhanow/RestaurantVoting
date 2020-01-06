package ru.restaurantVoting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.data.DishTestData;
import ru.restaurantVoting.model.Dish;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.repository.JpaUtil;
import ru.restaurantVoting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurantVoting.data.DishTestData.*;
import static ru.restaurantVoting.data.MenuTestData.assertMatch;
import static ru.restaurantVoting.data.MenuTestData.*;
import static ru.restaurantVoting.data.RestaurantTestData.*;

class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JpaUtil jpaUtil;

    @BeforeEach
    void setUp() throws Exception {
        cacheManager.getCache("menusByDate").clear();
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null, LocalDate.of(3000, 1, 1), RESTAURANT_1);
        Menu created = service.create(new Menu(newMenu), RESTAURANT_ID_1);
        newMenu.setId(created.getId());
        assertMatch(created, newMenu);
        assertMatch(service.getAll(), MENU_1, MENU_2, MENU_5, MENU_3, MENU_4, newMenu);
    }

    @Test
    void delete() throws Exception {
        service.delete(MENU_ID_1, RESTAURANT_ID_1);
        assertMatch(service.getAll(), MENU_2, MENU_5, MENU_3, MENU_4);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(MENU_ID_1, RESTAURANT_ID_2));
    }

    @Test
    void get() throws Exception {
        Menu menu = service.get(MENU_ID_1, RESTAURANT_ID_1);
        assertMatch(menu, MENU_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(MENU_ID_1, RESTAURANT_ID_2));
    }

    @Test
    void findByDate() throws Exception {
        List<Menu> menuList = service.findByDate(LocalDate.now());
        assertMatch(menuList, MENU_3, MENU_4);

        List<Dish> actualDishes = new ArrayList<>();
        menuList.forEach(menu -> actualDishes.addAll(menu.getDishes()));
        DishTestData.assertMatch(actualDishes, Arrays.asList(DISH_3, DISH_4, DISH_5, DISH_6));
    }

    @Test
    void findByRestaurant() throws Exception {
        List<Menu> menuList = service.findByRestaurant(RESTAURANT_2.getName());
        assertMatch(menuList, MENU_2, MENU_3);
    }

    @Test
    void findByRestaurantAndDate() throws Exception {
        Menu actual = service.findByRestaurantAndDate(RESTAURANT_2.getName(), LocalDate.now());
        assertMatch(actual, MENU_3);
    }

    @Test
    void findByRestaurantAndDateNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.findByRestaurantAndDate(RESTAURANT_1.getName(), LocalDate.now()));
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(MENU_1);
        updated.setDate(LocalDate.of(3000, 1, 1));
        updated.setRestaurant(RESTAURANT_1);
        service.update(new Menu(updated), RESTAURANT_ID_1);
        assertMatch(service.get(MENU_ID_1, RESTAURANT_ID_1), updated);
    }

    @Test
    void getAll() throws Exception {
        List<Menu> all = service.getAll();
        assertMatch(all, MENU_1, MENU_2, MENU_5, MENU_3, MENU_4);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Menu(null, null), RESTAURANT_ID_2), ConstraintViolationException.class);
    }
}