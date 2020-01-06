package ru.restaurantVoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.to.MenuTo;
import ru.restaurantVoting.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantVoting.TestUtil.readFromJson;
import static ru.restaurantVoting.TestUtil.userHttpBasic;
import static ru.restaurantVoting.data.DishTestData.DISH_1;
import static ru.restaurantVoting.data.DishTestData.DISH_3;
import static ru.restaurantVoting.data.MenuTestData.assertMatch;
import static ru.restaurantVoting.data.MenuTestData.contentJson;
import static ru.restaurantVoting.data.MenuTestData.*;
import static ru.restaurantVoting.data.RestaurantTestData.*;
import static ru.restaurantVoting.data.UserTestData.ADMIN;
import static ru.restaurantVoting.util.MenuUtil.menuFromTo;
import static ru.restaurantVoting.util.exception.ErrorType.DATA_ERROR;
import static ru.restaurantVoting.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.restaurantVoting.web.MenuController.RESTAURANT_URL;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MenuController.REST_URL + '/';

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MENU_1, MENU_2, MENU_5, MENU_3, MENU_4));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MENU_ID_1 + RESTAURANT_URL + RESTAURANT_ID_1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MENU_1));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 0 + RESTAURANT_URL + RESTAURANT_ID_2)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createWithLocation() throws Exception {
        MenuTo menuTo = new MenuTo(null, LocalDate.of(3000, 1, 1), DISH_1, DISH_3);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_URL + RESTAURANT_ID_2)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo)))
                .andExpect(status().isCreated());

        Menu returned = readFromJson(action, Menu.class);
        menuTo.setId(returned.getId());

        Menu expected = menuFromTo(menuTo);
        expected.setRestaurant(RESTAURANT_2);
        assertMatch(returned, menuFromTo(menuTo));
        assertMatch(menuService.getAll(), MENU_1, MENU_2, MENU_5, MENU_3, MENU_4, expected);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ID_1 + RESTAURANT_URL + RESTAURANT_ID_1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(menuService.getAll(), MENU_2, MENU_5, MENU_3, MENU_4);
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 0 + RESTAURANT_URL + RESTAURANT_ID_2)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        MenuTo updated = new MenuTo(MENU_ID_1, LocalDate.of(3000, 1, 1), DISH_1, DISH_3);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU_ID_1 + RESTAURANT_URL + RESTAURANT_ID_1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        Menu expected = menuFromTo(updated);
        expected.setRestaurant(RESTAURANT_1);
        assertMatch(menuService.get(MENU_ID_1, RESTAURANT_ID_1), expected);
    }

    @Test
    void findByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byDate?date=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MENU_3, MENU_4));
    }

    @Test
    void findByRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byRestaurant?name=" + RESTAURANT_2.getName())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MENU_2, MENU_3));
    }

    @Test
    void findByRestaurantAndDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byRestaurantAndDate?name=" + RESTAURANT_2.getName()
                + "&date=" + LocalDate.now())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MENU_3));
    }

    @Test
    void findByRestaurantAndDateNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byRestaurantAndDate?name=" + RESTAURANT_1.getName()
                + "&date=" + LocalDate.now())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, null);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_URL + RESTAURANT_ID_3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        MenuTo invalid = new MenuTo(MENU_ID_2, null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU_ID_2 + RESTAURANT_URL + RESTAURANT_ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(MENU_ID_2, LocalDate.now());
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU_ID_2 + RESTAURANT_URL + RESTAURANT_ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(DATA_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(null, LocalDate.now());
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_URL + RESTAURANT_ID_3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(DATA_ERROR));
    }
}