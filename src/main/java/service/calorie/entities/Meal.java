package service.calorie.entities;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:06
 * Purpose: TODO:
 **/
public class Meal {
    private long id;
    private LocalDate date;
    private LocalTime time;
    private String text;
    private int calories;
    private boolean lessThanExpected;

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean isLessThanExpected() {
        return lessThanExpected;
    }

    public void setLessThanExpected(boolean lessThanExpected) {
        this.lessThanExpected = lessThanExpected;
    }
}
