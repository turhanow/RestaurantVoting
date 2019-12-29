package ru.restaurantVoting.service;

import org.junit.jupiter.api.extension.ExtendWith;
import ru.restaurantVoting.AbstractTest;
import ru.restaurantVoting.TimingExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurantVoting.util.ValidationUtil.getRootCause;

@ExtendWith(TimingExtension.class)
abstract class AbstractServiceTest extends AbstractTest {


    <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}