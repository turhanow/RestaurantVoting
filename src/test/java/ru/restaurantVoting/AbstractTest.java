package ru.restaurantVoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.service.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml"
})
public abstract class AbstractTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected RestaurantService restaurantService;

    @Autowired
    protected DishService dishService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected VoteService voteService;
}