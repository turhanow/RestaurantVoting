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
import ru.restaurantVoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static ru.restaurantVoting.util.ValidationUtil.checkExpiredDate;
import static ru.restaurantVoting.util.ValidationUtil.checkExpiredDateWithTime;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/votes";
    public static final String MENUS_URL = "/menus/";

    @Autowired
    private VoteService voteService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/byDate")
    public List<Vote> findAllByDate(@RequestParam LocalDate date) {
        log.info("getAllByDate {}", date);
        return voteService.getAllByDate(date);
    }

    @PostMapping(MENUS_URL + "{menuId}")
    public ResponseEntity<Restaurant> vote(@PathVariable("menuId") int menuId) {
        int userId = SecurityUtil.authUserId();
        log.info("User with id = {} votes for menu with id = {}", userId, menuId);
        LocalDate today = LocalDate.now();
        Menu menu = menuService.findById(menuId);
        checkExpiredDate(menu.getDate(), menuId);

        Vote vote = voteService.getForUserAndDate(userId, today);

        if (Objects.isNull(vote)) {
            vote = new Vote(null, today);
            voteService.create(vote, userId, menuId);
        } else {
            throw new NotFoundException("Vote for menu id=" + menuId + " already exist!");
        }

        return new ResponseEntity<>(menu.getRestaurant(), HttpStatus.CREATED);
    }

    @PutMapping(MENUS_URL + "{menuId}")
    public ResponseEntity<Restaurant> reVote(@PathVariable("menuId") int menuId) {
        int userId = SecurityUtil.authUserId();
        log.info("User with id = {} updates vote for menu with id = {}", userId, menuId);
        LocalDate today = LocalDate.now();
        Menu menu = menuService.findById(menuId);

        checkExpiredDateWithTime(menu.getDate(), menuId);

        Vote vote = voteService.getForUserAndDate(userId, today);
        if (Objects.isNull(vote)) {
            throw new NotFoundException("Vote for menu id=" + menuId + " is not found!");
        } else {
            voteService.update(vote, userId, menuId);
        }
        return new ResponseEntity<>(menu.getRestaurant(), HttpStatus.OK);
    }
}