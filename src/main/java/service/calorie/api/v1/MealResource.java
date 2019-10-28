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

        Specification<Meal> spec = search == null ? null : SpecificationUtils.getSpecFromQuery(search, SpecificationUtils::mealAttributeConverter);
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
    public Meal addMeal(@Valid @RequestBody Meal meal, Principal principal) {

        if (meal.getCalories() == 0) {
            meal.setCalories(nutritionixService.fetchCaloriesFromText(meal.getText()));
        }
        User user = userRepository.findUserByUsername(principal.getName());
        meal.setUser(user);
        updateLessThanExpected(user, meal);
        return mealRepository.save(meal);
    }

    private void updateLessThanExpected(User user, Meal meal) {
        Long sum = mealRepository.sumOfCaloriesByUser(user.getId());
        sum = sum == null ? 0 : sum;
        int caloriePerDay = user.getUserSettings().getExpCaloriesPerDay();
        if (caloriePerDay == 0 || sum > caloriePerDay) {
            return;
        }
        if (sum + meal.getCalories() > caloriePerDay) {
            mealRepository.updateLessThanExpectedByUser(user.getId(), false);
        }
    }

    @PutMapping("/{meal_id}")
    public Meal updateMeal(Meal meal, @PathVariable("meal_id") long mealId, Principal principal)
            throws ForbiddenResourceException {
        Meal dbMeal = mealRepository.findById(mealId);
        User user = userRepository.findUserByUsername(principal.getName());

        if (dbMeal == null) {
            meal.setUser(user);
            updateLessThanExpected(user, meal);
            return mealRepository.save(meal);
        }
        if (!meal.getUser().getUsername().equals(principal.getName())) {
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
        dbMeal.setUser(user);
        updateLessThanExpected(user, dbMeal);
        return mealRepository.save(dbMeal);
    }

    @DeleteMapping("/{meal_id}")
    public void deleteMeal(@PathVariable("meal_id") long mealId, Principal principal)
            throws ResourceNotExistException, ForbiddenResourceException {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new ResourceNotExistException(String.format("No meal with id:%s", mealId));
        }
        if (!meal.getUser().getUsername().equals(principal.getName())) {
            throw new ForbiddenResourceException();
        }
        User user = userRepository.findUserByUsername(principal.getName());

        Long sum = mealRepository.sumOfCaloriesByUser(user.getId());
        sum = sum == null ? 0 : sum;
        int caloriePerDay = user.getUserSettings().getExpCaloriesPerDay();
        if (caloriePerDay == 0 || sum < caloriePerDay) {
            mealRepository.delete(meal);
            return;
        }
        if (sum - meal.getCalories() <= caloriePerDay) {
            mealRepository.updateLessThanExpectedByUser(user.getId(), true);
        }
        mealRepository.delete(meal);
    }
}
