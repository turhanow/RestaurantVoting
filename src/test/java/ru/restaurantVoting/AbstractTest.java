package ru.restaurantVoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.restaurantVoting.service.UserService;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml"
})
public abstract class AbstractTest {
    @Autowired
    protected UserService userService;
}