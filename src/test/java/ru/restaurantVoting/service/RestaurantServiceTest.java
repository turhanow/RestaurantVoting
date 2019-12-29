package ru.restaurantVoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.restaurantVoting.model.Restaurant;
import ru.restaurantVoting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurantVoting.data.RestaurantTestData.*;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "new Restaurant");
        Restaurant created = service.create(new Restaurant(newRestaurant));
        newRestaurant.setId(created.getId());
        assertMatch(created, newRestaurant);
        assertMatch(service.getAll(), RESTAURANT_3, RESTAURANT_1, RESTAURANT_2, newRestaurant);
    }

    @Test
    void delete() throws Exception {
        service.delete(RESTAURANT_ID_1);
        assertMatch(service.getAll(), RESTAURANT_3, RESTAURANT_2);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() throws Exception {
        Restaurant restaurant = service.get(RESTAURANT_ID_1);
        assertMatch(restaurant, RESTAURANT_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT_1);
        updated.setName("newName");
        service.update(new Restaurant(updated));
        assertMatch(service.get(RESTAURANT_ID_1), updated);
    }

    @Test
    void getAll() throws Exception {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT_3, RESTAURANT_1, RESTAURANT_2);
    }

    @Test
    void findByName() throws Exception {
        Restaurant restaurant = service.findByName(RESTAURANT_1.getName());
        assertMatch(restaurant, RESTAURANT_1);
    }

    @Test
    void findByNameNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.findByName("name"));
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
    }
}