package service.calorie.entities;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:06
 * Purpose: TODO:
 **/
public class Meal {
    private LocalDate date;
    private LocalTime time;
    private String text;
    private int calories;
    private boolean lessThanExpected;
}
