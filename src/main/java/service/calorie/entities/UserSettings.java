package service.calorie.entities;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:11
 * Purpose: Settings for a user.
 **/
@Embeddable
public class UserSettings {
    @NotNull
    private int expCaloriesPerDay;

    public int getExpCaloriesPerDay() {
        return expCaloriesPerDay;
    }

    public void setExpCaloriesPerDay(int expCaloriesPerDay) {
        this.expCaloriesPerDay = expCaloriesPerDay;
    }
}
