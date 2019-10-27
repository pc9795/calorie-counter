package service.calorie.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import service.calorie.entities.Meal;
import service.calorie.entities.User;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class TestMealRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MealRepository repository;

    @Test
    public void testFindAll() {
        User user = new User("admin", "admin");
        Meal meal1 = new Meal();
        meal1.setCalories(100);
        meal1.setText("food1");
        meal1.setLessThanExpected(true);
        meal1.setTime(LocalTime.MIDNIGHT);
        meal1.setDate(LocalDate.of(2019, 10, 27));

        Meal meal2 = new Meal();
        meal2.setCalories(200);
        meal2.setText("food2");
        meal2.setLessThanExpected(false);
        meal2.setTime(LocalTime.NOON);
        meal2.setDate(LocalDate.of(2019, 10, 28));

        user.addMeal(meal1);
        user.addMeal(meal2);

        testEntityManager.persist(user);
        testEntityManager.persist(meal1);
        testEntityManager.persist(meal2);
        testEntityManager.flush();

        Page<Meal> meals = repository.findAllByUser(user, null);
    }
}
