package service.calorie.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import service.calorie.entities.User;
import service.calorie.repositories.UserRepository;
import service.calorie.service.ApiUserDetailsService;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 21:30
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
public class TestApiUserDetailsService {

    @TestConfiguration
    static class Config {

        @Bean
        public ApiUserDetailsService apiUserDetailsService(UserRepository userRepository) {
            return new ApiUserDetailsService(userRepository);
        }
    }

    @Autowired
    private ApiUserDetailsService service;

    @MockBean
    public UserRepository repository;

    @Test
    public void testLoadUserByUsername() {
        User user = new User("admin", "xxx");
        Mockito.when(repository.findUserByUsername("admin")).thenReturn(user);
        assert service.loadUserByUsername("admin").getUsername().equals("admin");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameNegative() {
        Mockito.when(repository.findUserByUsername("admin")).thenReturn(null);
        service.loadUserByUsername("admin");
    }

}
