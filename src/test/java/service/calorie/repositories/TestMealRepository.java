package service.calorie.repositories;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import service.calorie.entities.Meal;
import service.calorie.entities.User;
import service.calorie.entities.UserSettings;
import service.calorie.exceptions.InvalidSearchQueryException;
import service.calorie.util.ApiSpecification;
import service.calorie.util.SearchCriteria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 21:49
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
//configuring H2, an in-memory database
//setting Hibernate, Spring Data, and the DataSource
//performing an @EntityScan
//turning on SQL logging
@DataJpaTest
//It will reset sequence so that id order is deterministic.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestMealRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MealRepository repository;

    private User user;
    private Meal[] meals;

    // NOTE: Because I am using DirtiesContext therefore after each test the ids will be reset and the id of the
    // entities are given on the basis of their save order. So these tests depend on these deterministic id order.
    @Before
    public void setup() {
        user = new User("admin", "admin123");
        UserSettings userSettings = new UserSettings();
        userSettings.setExpCaloriesPerDay(1200);
        user.setUserSettings(userSettings);

        meals = new Meal[5];
        meals[0] = new Meal();
        meals[0].setCalories(100);
        meals[0].setText("food1");
        meals[0].setLessThanExpected(true);
        meals[0].setTime(LocalTime.MIDNIGHT);
        meals[0].setDate(LocalDate.of(2019, 10, 27));

        meals[1] = new Meal();
        meals[1].setCalories(200);
        meals[1].setText("food2");
        meals[1].setLessThanExpected(false);
        meals[1].setTime(LocalTime.NOON);
        meals[1].setDate(LocalDate.of(2019, 10, 28));

        meals[2] = new Meal();
        meals[2].setCalories(300);
        meals[2].setText("food3");
        meals[2].setLessThanExpected(true);
        meals[2].setTime(LocalTime.MIDNIGHT);
        meals[2].setDate(LocalDate.of(2019, 10, 29));

        meals[3] = new Meal();
        meals[3].setCalories(400);
        meals[3].setText("food4");
        meals[3].setLessThanExpected(false);
        meals[3].setTime(LocalTime.NOON);
        meals[3].setDate(LocalDate.of(2019, 10, 30));

        meals[4] = new Meal();
        meals[4].setCalories(500);
        meals[4].setText("food5");
        meals[4].setLessThanExpected(true);
        meals[4].setTime(LocalTime.MIDNIGHT);
        meals[4].setDate(LocalDate.of(2019, 11, 1));

        // Will give it id of 1.
        for (Meal meal : meals) {
            user.addMeal(meal);
        }
        // Meals will have ids of 2, 3, 4, 5 respectively.
        testEntityManager.persist(user);
        for (Meal meal : meals) {
            testEntityManager.persist(meal);
        }
        testEntityManager.flush();

    }

    @Test(expected = NullPointerException.class)
    public void testFindAllWithNullPageable() {
        repository.findAll(null, null).getContent();
    }

    @Test
    public void testFindAllWithUser() {
        List<Meal> dbMeals = repository.findAll(new ApiSpecification(
                new SearchCriteria("user", user, "eq")), Pageable.unpaged()).getContent();
        assert dbMeals.size() == meals.length;
        for (Meal meal : meals) {
            assert dbMeals.contains(meal);
        }
    }

    @Test
    public void testFindAllWithPageable() {
        List<Meal> dbMeals = repository.
                findAll(null, PageRequest.of(0, 2, Sort.by("id"))).getContent();
        assert dbMeals.size() == 2;
        assert dbMeals.contains(meals[0]);
        assert dbMeals.contains(meals[1]);

        dbMeals = repository.findAll(null, PageRequest.of(0, 10, Sort.by("id"))).getContent();
        assert dbMeals.size() == meals.length;
        for (Meal meal : meals) {
            assert dbMeals.contains(meal);
        }

        dbMeals = repository.findAll(null, PageRequest.of(1, 6, Sort.by("id"))).getContent();
        assert dbMeals.size() == 0;
    }


    @Test
    public void testFindAllWithUserAndSpec() {
        ApiSpecification spec1 = new ApiSpecification(new SearchCriteria("user", user, "eq"));
        ApiSpecification spec2 = new ApiSpecification(new SearchCriteria("id", 2, "eq"));
        assert repository.findAll(spec1.and(spec2), Pageable.unpaged()).getContent().size() == 1;
    }


    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testFindAllInvalidSpec() {
        repository.findAll(new ApiSpecification(new SearchCriteria("xxx", "temp_value", "eq")),
                Pageable.unpaged());
    }

    @Test
    public void testFindAll() throws InvalidSearchQueryException {
        List<Meal> meals = repository.findAll(null, Pageable.unpaged()).getContent();
        assert meals.size() == 5;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("id", 2, "eq")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 1;
        assert meals.get(0).getId() == 2;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("id", 3, "gt")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 3;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("calories", 300, "eq")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 1;
        assert meals.get(0).getCalories() == 300;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("calories", 400, "ge")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 2;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("text", "food1", "eq")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 1;
        assert meals.get(0).getText().equals("food1");

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("text", "food4", "le")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 4;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("date", LocalDate.of(2019, 10, 27),
                        "eq")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 1;
        assert meals.get(0).getDate().equals(LocalDate.of(2019, 10, 27));

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("date", LocalDate.of(2019, 10, 27),
                        "gt")), Pageable.unpaged()).
                getContent();
        assert meals.size() == 4;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("time", LocalTime.NOON, "eq")),
                Pageable.unpaged()).getContent();
        assert meals.size() == 2;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("time", LocalTime.MIDNIGHT, "lt")),
                Pageable.unpaged()).getContent();
        assert meals.size() == 0;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("lessThanExpected", true, "eq")),
                Pageable.unpaged()).getContent();
        assert meals.size() == 3;

        meals = repository.findAll(
                new ApiSpecification(new SearchCriteria("lessThanExpected", false, "lt")),
                Pageable.unpaged()).getContent();
        // here false and true can be imagined as 0 and 1 for comparison logic.
        assert meals.size() == 0;
    }

    @Test
    public void testFindById() {
        assert repository.findById(2).getId() == 2;
        assert repository.findById(-1) == null;
    }

    @Test
    public void testSumOfCaloriesByUser() {
        assert repository.sumOfCaloriesByUser(1) == 1500;
    }

    @Test
    public void testUpdateLessThanExpectedByUser() {
        repository.updateLessThanExpectedByUser(1, true);
        assert repository.findAll(new ApiSpecification(new SearchCriteria("lessThanExpected", true,
                "eq")), Pageable.unpaged()).getContent().size() == meals.length;
    }

    @After
    public void clean() {
        repository.deleteAll();
    }

}
