package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.calorie.beans.ApiUserPrincipal;
import service.calorie.entities.Meal;
import service.calorie.exceptions.ForbiddenResourceException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.repositories.MealRepository;
import service.calorie.repositories.UserRepository;
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

    @Autowired
    public MealResource(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Meal> getMeals(ApiUserPrincipal principal) {
        if (principal.getUser().isAdmin()) {
            return mealRepository.findAll();
        }
        return mealRepository.findAllByUser(principal.getUser());
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
