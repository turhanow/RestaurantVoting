package ru.restaurantVoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.model.Vote;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantVoting.TestUtil.userHttpBasic;
import static ru.restaurantVoting.data.MenuTestData.*;
import static ru.restaurantVoting.data.UserTestData.*;
import static ru.restaurantVoting.data.VoteTestData.assertMatch;
import static ru.restaurantVoting.data.VoteTestData.contentJson;
import static ru.restaurantVoting.data.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';
    private static final String MENUS_MAPPING = "menus/";

    @Test
    void voteExpiredDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_MAPPING + MENU_ID_2)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void vote() throws Exception {
        LocalDate today = LocalDate.now();
        if (MENU_3.getDate().isEqual(today)) {
            mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_MAPPING + MENU_ID_3)
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isOk());
        } else {
            mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_MAPPING + MENU_ID_3)
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void voteDuplicate() throws Exception {
        if (MENU_3.getDate().isEqual(LocalDate.now())) {
            mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_MAPPING + MENU_ID_3)
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        } else {
            mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + MENUS_MAPPING + MENU_ID_3)
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    void updateVote() throws Exception {
        if (MENU_4.getDate().isEqual(LocalDate.now())) {
            Vote update = new Vote(VOTE_3);
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENUS_MAPPING + MENU_ID_4)
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isOk());
            assertMatch(voteService.getForUserAndDate(USER_ID, VOTE_3.getDate()), update);
        } else {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENUS_MAPPING + MENU_ID_4)
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    void updateVoteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENUS_MAPPING + MENU_ID_4)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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