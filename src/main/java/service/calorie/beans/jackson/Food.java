package service.calorie.beans.jackson;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 01:48
 * Purpose: TODO:
 **/
public class Food {
    private String food_name;
    private String brand_name;
    private float serving_qty;
    private String serving_unit;
    private float serving_weight_grams;
    private float nf_calories;
    private float nf_total_fat;
    private float nf_saturated_fat;
    private float nf_cholestrol;
    private float nf_sodium;
    private float nf_total_carbohydrate;
    private float nf_dietary_fiber;
    private float nf_sugars;
    private float nf_protien;
    private float nf_potassium;
    private float nf_p;
    private List<Nutrient> full_nutrients;
    private String nix_brand_name;
    private int nix_brand_id;
    private String nix_item_name;
    private int nix_item_id;
    private String upc;
    private String consumed_at;

}
