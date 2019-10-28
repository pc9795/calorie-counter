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
import service.calorie.entities.User;
import service.calorie.entities.UserSettings;
import service.calorie.utils.ApiSpecification;
import service.calorie.utils.SearchCriteria;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 01:46
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestUserRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository repository;

    private User[] users;

    @Before
    public void setup() {
        users = new User[5];
        users[0] = new User("admin", "admin123");
        UserSettings userSettings = new UserSettings();
        userSettings.setExpCaloriesPerDay(1000);
        users[0].setUserSettings(userSettings);

        users[1] = new User("admin2", "admin123");
        UserSettings userSettings2 = new UserSettings();
        userSettings2.setExpCaloriesPerDay(2000);
        users[1].setUserSettings(userSettings2);

        users[2] = new User("admin3", "admin123");
        UserSettings userSettings3 = new UserSettings();
        userSettings3.setExpCaloriesPerDay(3000);
        users[2].setUserSettings(userSettings3);

        users[3] = new User("admin4", "admin123");
        UserSettings userSettings4 = new UserSettings();
        userSettings4.setExpCaloriesPerDay(4000);
        users[3].setUserSettings(userSettings4);

        users[4] = new User("admin5", "admin123");
        UserSettings userSettings5 = new UserSettings();
        userSettings5.setExpCaloriesPerDay(5000);
        users[4].setUserSettings(userSettings5);

        for (User user : users) {
            testEntityManager.persist(user);
        }
        testEntityManager.flush();
    }

    @Test
    public void testFindUserByUsername() {
        assert repository.findUserByUsername("admin").getUsername().equals("admin");
        assert repository.findUserByUsername("xxx") == null;
    }

    @Test
    public void testFindUserById() {
        assert repository.findById(1).getId() == 1;
        assert repository.findById(-1) == null;
    }

    @Test(expected = NullPointerException.class)
    public void testFindAllWithNullPageable() {
        repository.findAll(null, null);
    }

    @Test
    public void testFindallWithPageable() {
        List<User> dbUsers = repository.
                findAll(null, PageRequest.of(0, 2, Sort.by("id"))).getContent();
        assert dbUsers.size() == 2;
        assert dbUsers.contains(users[0]);
        assert dbUsers.contains(users[1]);

        dbUsers = repository.findAll(null, PageRequest.of(0, 10, Sort.by("id"))).getContent();
        assert dbUsers.size() == users.length;
        for (User user : users) {
            assert dbUsers.contains(user);
        }

        dbUsers = repository.findAll(null, PageRequest.of(1, 6, Sort.by("id"))).getContent();
        assert dbUsers.size() == 0;
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testFindallInvalidSpec() {
        repository.findAll(new ApiSpecification<>(new SearchCriteria("xxx", "temp_value", "eq")),
                Pageable.unpaged());
    }

    @Test
    public void testFindAll() {
        List<User> users = repository.findAll(null, Pageable.unpaged()).getContent();
        assert users.size() == 5;

        users = repository.findAll(new ApiSpecification(new SearchCriteria("id", 1, "eq")),
                Pageable.unpaged()).getContent();
        assert users.size() == 1;

        users = repository.findAll(new ApiSpecification(new SearchCriteria("id", 2, "gt")),
                Pageable.unpaged()).getContent();
        assert users.size() == 3;

        users = repository.findAll(new ApiSpecification(new SearchCriteria("username", "admin", "eq")),
                Pageable.unpaged()).getContent();
        assert users.size() == 1;

        users = repository.findAll(new ApiSpecification(new SearchCriteria("username", "admin4", "le")),
                Pageable.unpaged()).getContent();
        assert users.size() == 4;

    }


    @After
    public void clean() {
        repository.deleteAll();
    }

}
