package service.calorie.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import service.calorie.entities.Meal;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 00:19
 * Purpose: Repository for meal entity.
 **/
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {
    Page<Meal> findAll(Specification specification, Pageable pageable);

    Meal findById(long id);

    @Query("SELECT sum(m.calories) from Meal m where m.user.id=:userId")
    Long sumOfCaloriesByUser(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Meal m set m.lessThanExpected=:lessThanExpected where m.user.id=:userId")
    void updateLessThanExpectedByUser(@Param("userId") long id, @Param("lessThanExpected") boolean lessThanExpected);
}
