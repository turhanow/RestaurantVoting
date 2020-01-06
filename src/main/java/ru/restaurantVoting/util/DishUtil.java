package ru.restaurantVoting.util;

import ru.restaurantVoting.model.Dish;
import ru.restaurantVoting.to.DishTo;

public class DishUtil {

    private DishUtil() {
    }

    public static Dish dishFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }
}