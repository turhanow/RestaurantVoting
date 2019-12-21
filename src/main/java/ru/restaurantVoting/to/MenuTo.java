package ru.restaurantVoting.to;

import ru.restaurantVoting.model.AbstractBaseEntity;
import ru.restaurantVoting.model.Dish;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class MenuTo extends AbstractBaseEntity {

    @NotNull
    private final LocalDate date;

    @NotNull
    private final List<Dish> menu;

    public MenuTo(Integer id, LocalDate date, Dish... dishes) {
        super(id);
        this.date = date;
        this.menu = List.of(dishes);
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + date +
                ", menu=" + menu +
                '}';
    }
}