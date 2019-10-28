package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import service.calorie.config.ApiUserPrincipal;
import service.calorie.entities.Meal;
import service.calorie.exceptions.ForbiddenResourceException;
import service.calorie.exceptions.InvalidDataException;
import service.calorie.exceptions.InvalidSearchQueryException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.repositories.MealRepository;
import service.calorie.repositories.UserRepository;
import service.calorie.service.NutritionixService;
import service.calorie.utils.Constants;
import service.calorie.utils.SpecificationUtils;

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
    public List<Meal> getMeals(ApiUserPrincipal principal, Pageable pageable,
                               @RequestParam(value = "search", required = false) String search)
            throws InvalidDataException {
        if (search == null) {
            if (principal.getUser().isAdmin()) {
                return mealRepository.findAll(pageable).getContent();
            }
//            return mealRepository.findAllByUser(principal.getUser(), pageable).getContent();
            return null;
        }
        try {
            Specification<Meal> spec = SpecificationUtils.getSpecFromQuery(search);
            return mealRepository.findAll(spec, pageable).getContent();

        } catch (InvalidSearchQueryException e) {
            throw new InvalidDataException(String.format("Invalid search query: %s", search));
        }

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
//        long sum = mealRepository.sumOfMealsForUser(principal.getUser().getId());
        long sum = 0;
        int caloriePerDay = principal.getUser().getUserSettings().getExpCaloriesPerDay();
        if (sum > caloriePerDay) {
            userRepository.save(principal.getUser());
            return meal;
        }
        if (sum + meal.getCalories() > principal.getUser().getUserSettings().getExpCaloriesPerDay()) {
//            mealRepository.updateLessThanExpected(principal.getUser().getId(), false);
        }
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
        principal.getUser().addMeal(meal);
//        long sum = mealRepository.sumOfMealsForUser(principal.getUser().getId());
        long sum = 0;
        int caloriePerDay = principal.getUser().getUserSettings().getExpCaloriesPerDay();
        if (sum > caloriePerDay) {
            return mealRepository.save(dbMeal);
        }
        if (sum + meal.getCalories() > principal.getUser().getUserSettings().getExpCaloriesPerDay()) {
//            mealRepository.updateLessThanExpected(principal.getUser().getId(), false);
        }
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

//        long sum = mealRepository.sumOfMealsForUser(principal.getUser().getId());
        long sum = 0;
        int caloriePerDay = principal.getUser().getUserSettings().getExpCaloriesPerDay();
        if (sum < caloriePerDay) {
            mealRepository.delete(meal);
            return;
        }
        if (sum - meal.getCalories() <= principal.getUser().getUserSettings().getExpCaloriesPerDay()) {
//            mealRepository.updateLessThanExpected(principal.getUser().getId(), true);
        }
        mealRepository.delete(meal);
    }
}
