package ru.restaurantVoting.util;

import ru.restaurantVoting.model.Menu;
import ru.restaurantVoting.to.MenuTo;

public class MenuUtil {

    private MenuUtil() {
    }

    public static Menu menuFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getDate());
    }
}