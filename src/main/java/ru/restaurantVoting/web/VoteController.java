package ru.restaurantVoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.model.Restaurant;
import ru.restaurantVoting.model.Vote;
import ru.restaurantVoting.service.MenuService;
import ru.restaurantVoting.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final LocalTime EXPIRED_TIME = LocalTime.parse("11:00");

    public static final String REST_URL = "/rest/votes";

    @Autowired
    private VoteService voteService;

    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll");
        return voteService.getAll();
    }

    @PostMapping("/{menuId}")
    public ResponseEntity<Restaurant> vote(@PathVariable("menuId") int menuId) {
        int userId = SecurityUtil.authUserId();
        log.info("User with id = {} votes for menu with id = {}", userId, menuId);
        LocalDate today = LocalDate.now();
        boolean expired = LocalTime.now().isAfter(EXPIRED_TIME);
        Menu menu = menuService.findById(menuId);
        if (expired) {
            return new ResponseEntity<>(menu.getRestaurant(), HttpStatus.CONFLICT);
        }

        if (!menu.getDate().equals(today)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vote vote = voteService.getForUserAndDate(userId, today);
        if (Objects.nonNull(vote)) {
            vote.setMenu(menu);
            voteService.update(vote, userId, menuId);
            return new ResponseEntity<>(menu.getRestaurant(), HttpStatus.OK);
        } else {
            vote = new Vote(null, today);
            voteService.create(vote, userId, menuId);
            return new ResponseEntity<>(menu.getRestaurant(), HttpStatus.CREATED);
        }
    }
}