package ru.restaurantVoting.to;

import ru.restaurantVoting.model.Dish;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class MenuTo extends BaseTo {

    @NotNull
    private LocalDate date;

    @NotNull
    private Dish[] dishes;

    public MenuTo() {
    }

    public MenuTo(Integer id, LocalDate date, Dish... dishes) {
        super(id);
        this.date = date;
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Dish[] getDishes() {
        return dishes;
    }

    public void setDishes(Dish... dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + date +
                ", dishes=" + dishes +
                '}';
    }
}