package ru.restaurantVoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurantVoting.data.MenuTestData.*;
import static ru.restaurantVoting.data.RestaurantTestData.RESTAURANT_ID_1;
import static ru.restaurantVoting.data.RestaurantTestData.RESTAURANT_ID_2;

class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null, LocalDate.now());
        Menu created = service.create(new Menu(newMenu), RESTAURANT_ID_1);
        newMenu.setId(created.getId());
        assertMatch(created, newMenu);
        assertMatch(service.getAll(), MENU_1, MENU_2, MENU_3, MENU_4, MENU_5, newMenu);
    }

    @Test
    void delete() throws Exception {
        service.delete(MENU_ID_1, RESTAURANT_ID_1);
        assertMatch(service.getAll(), MENU_2, MENU_3, MENU_4, MENU_5);
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
        List<Menu> menuList = service.findByDate(LocalDate.of(2019, 6, 11));
        assertMatch(menuList, MENU_3, MENU_4);
    }

    @Test
    void findByDateNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.findByDate(LocalDate.of(3000, 1, 1)));
    }

    @Test
    void findByRestaurant() throws Exception {
        List<Menu> menuList = service.findByRestaurant(RESTAURANT_ID_2);
        assertMatch(menuList, MENU_2, MENU_3);
    }

    @Test
    void findByRestaurantNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.findByRestaurant(1));
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(MENU_1);
        updated.setDate(LocalDate.of(3000, 1, 1));
        service.update(new Menu(updated), RESTAURANT_ID_1);
        assertMatch(service.get(MENU_ID_1, RESTAURANT_ID_1), updated);
    }

    @Test
    void getAll() throws Exception {
        List<Menu> all = service.getAll();
        assertMatch(all, MENU_1, MENU_2, MENU_3, MENU_4, MENU_5);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Menu(null, null), RESTAURANT_ID_2), ConstraintViolationException.class);
    }
}