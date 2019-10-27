package service.calorie.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import service.calorie.entities.Meal;
import service.calorie.entities.User;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 00:19
 * Purpose: TODO:
 **/
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {
    List<Meal> findAll();

    Page<Meal> findAllByUser(User user, Pageable pageable);

    Meal findById(long id);

    Page<Meal> findAll(Specification specification, Pageable pageable);

    @Query("SELECT sum(m.calories) from Meal m where user_id=:userId")
    long sumOfMealsForUser(@Param("userId") long userId);

    @Query("UPDATE Meal set lessThanExpected=:value where user_id=:userId")
    int updateLessThanExpected(@Param("userId") long id, @Param("value") boolean value);
}
