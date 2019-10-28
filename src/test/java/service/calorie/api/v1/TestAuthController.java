package service.calorie.api.v1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.calorie.config.RestAuthenticationEntryPoint;
import service.calorie.entities.User;
import service.calorie.repositories.UserRepository;
import service.calorie.service.ApiUserDetailsService;
import service.calorie.utils.Constants;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 03:45
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class TestAuthController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private UserRepository repository;

    @MockBean
    private ApiUserDetailsService apiUserDetailsService;

    @MockBean
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Test
    public void testRegisterUserExist() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(new User("admin", "password123"));

        mvc.perform(post("/register").
                param("username", "admin").
                param("password", "admin123")).
                andExpect(status().isInternalServerError()).
                andExpect(jsonPath("$.error.code").value(500)).
                andExpect(jsonPath("$.error.message").value(Constants.ErrorMsg.USER_ALREADY_EXISTS));
    }

    @Test
    public void testRegister() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(null);
        BDDMockito.when(passwordEncoder.encode("admin123")).
                then((invocationOnMock) -> invocationOnMock.getArgument(0));

        BDDMockito.when(repository.save(Mockito.any(User.class))).
                then((invocationOnMock) -> invocationOnMock.getArgument(0));

        mvc.perform(post("/register").
                param("username", "admin").param("password", "admin123")).
                andExpect(status().isCreated()).
                andExpect(jsonPath("$.username").value("admin")).
                andExpect(jsonPath("$.roles[0].type").value("REGULAR")).
                andExpect(jsonPath("$.userSettings.expCaloriesPerDay").value(0));

    }

    @Test
    public void testLoginConstraintViolation() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(null);
        BDDMockito.when(passwordEncoder.encode("admin")).
                then((invocationOnMock) -> invocationOnMock.getArgument(0));

        mvc.perform(post("/register").
                param("username", "admin").
                param("password", "admin")).
                andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUsernameNotFound() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(null);
        mvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "admin123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginPasswordNotMatch() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(new User("admin", "admin123"));
        //Simulating the case where encoded password is different.
        BDDMockito.when(passwordEncoder.encode("admin123")).
                then((invocationOnMock) -> invocationOnMock.getArgument(0) + "xxx");
        BDDMockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .then(invocation -> invocation.getArgument(0).equals(invocation.getArgument(1)));

        mvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "admin123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test_user")
    public void testLoginAlreadyLoggedIn() throws Exception {
        BDDMockito.given(repository.findUserByUsername("admin")).
                willReturn(new User("admin", "admin123"));
        //Simulating the case where encoded password is different.
        BDDMockito.when(passwordEncoder.encode("admin123")).
                then((invocationOnMock) -> invocationOnMock.getArgument(0));
        BDDMockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .then(invocation -> invocation.getArgument(0).equals(invocation.getArgument(1)));

        mvc.perform(post("/login").
                param("username", "admin")
                .param("password", "admin123"))
                .andExpect(status().isBadRequest());
    }
}
