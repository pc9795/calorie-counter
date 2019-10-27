package service.calorie.beans.jackson;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:48
 * Purpose: TODO:
 **/
public class Foods {
    private List<Food> foods;

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
