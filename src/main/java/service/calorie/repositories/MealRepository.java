package service.calorie.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
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

    List<Meal> findAllByUser(User user);

    Meal findById(long id);
}
