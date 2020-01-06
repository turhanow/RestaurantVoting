package ru.restaurantVoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantVoting.TestUtil.userHttpBasic;
import static ru.restaurantVoting.data.MenuTestData.*;
import static ru.restaurantVoting.data.UserTestData.ADMIN;
import static ru.restaurantVoting.data.UserTestData.USER;
import static ru.restaurantVoting.data.VoteTestData.VOTE_3;
import static ru.restaurantVoting.data.VoteTestData.contentJson;
import static ru.restaurantVoting.web.VoteController.MENUS_URL;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';

    @Test
    void voteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENU_ID_2)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_URL + MENU_ID_3)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(!MENU_3.getDate().isEqual(LocalDate.now()) ? status().isConflict() : status().isCreated());
    }

    @Test
    void getAllByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byDate?date=" + LocalDate.now())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(Collections.singletonList(VOTE_3)));
    }

}