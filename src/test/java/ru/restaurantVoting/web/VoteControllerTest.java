package ru.restaurantVoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantVoting.TestUtil.userHttpBasic;
import static ru.restaurantVoting.data.MenuTestData.MENU_ID_2;
import static ru.restaurantVoting.data.MenuTestData.MENU_ID_4;
import static ru.restaurantVoting.data.UserTestData.ADMIN;
import static ru.restaurantVoting.data.UserTestData.USER;
import static ru.restaurantVoting.data.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_1, VOTE_2, VOTE_3));
    }

    @Test
    void voteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENU_ID_2)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENU_ID_4)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(LocalTime.now().isAfter(VoteController.EXPIRED_TIME) ? status().isConflict() : status().isOk());
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