package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import service.calorie.entities.Meal;
import service.calorie.entities.User;
import service.calorie.exceptions.ForbiddenResourceException;
import service.calorie.exceptions.InvalidDataException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.repositories.MealRepository;
import service.calorie.repositories.UserRepository;
import service.calorie.service.NutritionixService;
import service.calorie.utils.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:15
 * Purpose: REST meal resource
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

    /**
     * Read all the meals.
     *
     * @param pageable
     * @param search
     * @param principal
     * @param userId
     * @return
     * @throws InvalidDataException
     */
    @GetMapping
    public List<Meal> getMeals(Pageable pageable, @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "user_id", required = false) Long userId, Principal principal)
            throws InvalidDataException, ResourceNotExistException, ForbiddenResourceException {
        boolean isAdmin = Utils.isPrincipalAdmin(principal);
        //Non admin can't view all.
        if (!isAdmin && userId == null) {
            throw new ForbiddenResourceException();
        }

        Specification<Meal> spec = search == null ? null : SpecificationUtils.getSpecFromQuery(search,
                SpecificationUtils::mealAttributeConverter);
        if (userId != null) {
            User user = userRepository.findById(userId.longValue());
            if (user == null) {
                throw new ResourceNotExistException(String.format("No user with id: %s", userId));
            }
            if (!isAdmin && !principal.getName().equals(user.getUsername())) {
                throw new ForbiddenResourceException();
            }
            //Add the user to the specification.
            Specification<Meal> userSpec = new ApiSpecification<>(new SearchCriteria("user", user, "eq"));
            spec = spec == null ? userSpec : spec.and(userSpec);
        }
        return mealRepository.findAll(spec, pageable).getContent();
    }

    /**
     * Get a single meal.
     *
     * @param mealId
     * @param principal
     * @return
     * @throws ResourceNotExistException
     * @throws ForbiddenResourceException
     */
    @GetMapping("/{meal_id}")
    public Meal getMeal(@PathVariable("meal_id") long mealId, Principal principal) throws ResourceNotExistException,
            ForbiddenResourceException {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new ResourceNotExistException(String.format("No meal with id:%s", mealId));
        }
        User user = userRepository.findUserByUsername(principal.getName());
        if (!Utils.isPrincipalAdmin(principal) && user.getId() != meal.getUser().getId()) {
            throw new ForbiddenResourceException();
        }
        return meal;
    }

    /**
     * Add a meal
     *
     * @param meal
     * @param principal
     * @return
     */
    @PostMapping
    public Meal addMeal(@RequestBody @Valid Meal meal, Principal principal) {
        if (meal.getCalories() == 0) {
            meal.setCalories(nutritionixService.fetchCaloriesFromText(meal.getText()));
        }
        User user = userRepository.findUserByUsername(principal.getName());
        meal.setUser(user);
        // It would be true at the beginning.
        meal.setLessThanExpected(true);
        updateLessThanExpected(user, meal, meal.getCalories(), true);
        return mealRepository.save(meal);
    }

    /**
     * Helper method to update 'lessThanExpected' field of meal according to user's calorie setting.
     *
     * @param user
     * @param meal
     */
    private void updateLessThanExpected(User user, Meal meal, int delta, boolean addition) {
        Long sum = mealRepository.sumOfCaloriesByUser(user.getId());
        sum = sum == null ? 0 : sum;
        int caloriePerDay = user.getUserSettings().getExpCaloriesPerDay();
        if (caloriePerDay == 0) {
            return;
        }
        if (addition && sum > caloriePerDay) {
            meal.setLessThanExpected(false);
            return;
        }
        if (!addition && sum <= caloriePerDay) {
            return;
        }
        if (addition && (sum + delta) > caloriePerDay) {
            meal.setLessThanExpected(false);
            mealRepository.updateLessThanExpectedByUser(user.getId(), false);
        }
        if (!addition && (sum - delta) <= caloriePerDay) {
            mealRepository.updateLessThanExpectedByUser(user.getId(), true);
        }
    }

    /**
     * Update a meal.
     *
     * @param meal
     * @param mealId
     * @param principal
     * @return
     * @throws ForbiddenResourceException
     */
    @PutMapping("/{meal_id}")
    public Meal updateMeal(@RequestBody @Valid Meal meal, @PathVariable("meal_id") long mealId, Principal principal)
            throws ForbiddenResourceException {
        if (meal.getCalories() == 0) {
            meal.setCalories(nutritionixService.fetchCaloriesFromText(meal.getText()));
        }
        // It would be true at the beginning.
        meal.setLessThanExpected(true);

        Meal dbMeal = mealRepository.findById(mealId);
        User user = userRepository.findUserByUsername(principal.getName());

        if (dbMeal == null) {
            meal.setUser(user);
            updateLessThanExpected(user, meal, meal.getCalories(), true);
            return mealRepository.save(meal);
        }
        if (!Utils.isPrincipalAdmin(principal) && !dbMeal.getUser().getUsername().equals(principal.getName())) {
            throw new ForbiddenResourceException();
        }
        int delta = meal.getCalories() - dbMeal.getCalories();
        boolean addition = delta >= 0;
        dbMeal.setCalories(meal.getCalories());
        dbMeal.setDate(meal.getDate());
        dbMeal.setTime(meal.getTime());
        dbMeal.setText(meal.getText());
        updateLessThanExpected(dbMeal.getUser(), dbMeal, Math.abs(delta), addition);
        return mealRepository.save(dbMeal);
    }

    /**
     * Delete a meal.
     *
     * @param mealId
     * @param principal
     * @throws ResourceNotExistException
     * @throws ForbiddenResourceException
     */
    @DeleteMapping("/{meal_id}")
    public void deleteMeal(@PathVariable("meal_id") long mealId, Principal principal)
            throws ResourceNotExistException, ForbiddenResourceException {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new ResourceNotExistException(String.format("No meal with id:%s", mealId));
        }
        if (!Utils.isPrincipalAdmin(principal) && !meal.getUser().getUsername().equals(principal.getName())) {
            throw new ForbiddenResourceException();
        }
        updateLessThanExpected(meal.getUser(), meal, meal.getCalories(), false);
        mealRepository.delete(meal);
    }
}
