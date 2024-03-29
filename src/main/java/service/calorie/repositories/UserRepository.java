package service.calorie.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import service.calorie.entities.User;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 01:50
 * Purpose: Repository for User entity.
 **/
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findUserByUsername(String username);

    User findById(long id);

    Page<User> findAll(Specification specification, Pageable pageable);
}
