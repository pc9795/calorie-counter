package service.calorie.beans.jackson;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:53
 * Purpose: TODO:
 **/
public class Nutrient {
    private int attr_id;
    private float value;

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
