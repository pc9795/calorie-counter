package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import service.calorie.beans.ApiUserPrincipal;
import service.calorie.entities.Meal;
import service.calorie.exceptions.ForbiddenResourceException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.repositories.MealRepository;
import service.calorie.repositories.UserRepository;
import service.calorie.service.NutritionixService;
import service.calorie.util.Constants;

import javax.validation.Valid;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:15
 * Purpose: TODO:
 **/
@RestController()
@RequestMapping(Constants.ApiV1Resource.MEAL)
public class MealResource {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final NutritionixService nutritionixService;

    @Autowired
    public MealResource(MealRepository mealRepository, UserRepository userRepository,
                        NutritionixService nutritionixService) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.nutritionixService = nutritionixService;
    }

    @GetMapping
    public List<Meal> getMeals(ApiUserPrincipal principal, Pageable pageable) {
        if (principal.getUser().isAdmin()) {
            return mealRepository.findAll(pageable).getContent();
        }
        return mealRepository.findAllByUser(principal.getUser(), pageable).getContent();
    }

    @GetMapping("/{meal_id}")
    public Meal getMeal(@PathVariable("meal_id") long mealId, ApiUserPrincipal principal)
            throws ResourceNotExistException, ForbiddenResourceException {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new ResourceNotExistException(String.format("No meal with id:%s", mealId));
        }
        if (meal.getUser().getId() != principal.getUser().getId()) {
            throw new ForbiddenResourceException();
        }
        return meal;
    }

    @PostMapping
    public Meal addMeal(@Valid Meal meal, ApiUserPrincipal principal) {
        if (meal.getCalories() == 0) {
            meal.setCalories(nutritionixService.fetchCaloriesFromText(meal.getText()));
        }
        principal.getUser().addMeal(meal);
        userRepository.save(principal.getUser());
        return meal;
    }

    @PutMapping("/{meal_id}")
    public Meal updateMeal(Meal meal, @PathVariable("meal_id") long mealId, ApiUserPrincipal principal)
            throws ForbiddenResourceException {
        Meal dbMeal = mealRepository.findById(mealId);
        if (dbMeal == null) {
            principal.getUser().addMeal(meal);
            userRepository.save(principal.getUser());
            return meal;
        }
        if (meal.getUser().getId() != principal.getUser().getId()) {
            throw new ForbiddenResourceException();
        }
        if (meal.getCalories() == 0) {
            meal.setCalories(nutritionixService.fetchCaloriesFromText(meal.getText()));
        }
        dbMeal.setCalories(meal.getCalories());
        dbMeal.setDate(meal.getDate());
        dbMeal.setTime(meal.getTime());
        dbMeal.setLessThanExpected(meal.isLessThanExpected());
        dbMeal.setText(meal.getText());
        return mealRepository.save(dbMeal);
    }

    @DeleteMapping("/{meal_id}")
    public void deleteMeal(@PathVariable("meal_id") long mealId, ApiUserPrincipal principal)
            throws ResourceNotExistException, ForbiddenResourceException {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new ResourceNotExistException(String.format("No meal with id:%s", mealId));
        }
        if (meal.getUser().getId() != principal.getUser().getId()) {
            throw new ForbiddenResourceException();
        }
        mealRepository.delete(meal);
    }
}
