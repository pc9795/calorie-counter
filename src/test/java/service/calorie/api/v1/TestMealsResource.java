package service.calorie.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.calorie.config.RestAuthenticationEntryPoint;
import service.calorie.config.SecurityConfig;
import service.calorie.entities.Meal;
import service.calorie.repositories.MealRepository;
import service.calorie.service.ApiUserDetailsService;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 04:12
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
@WebMvcTest(MealResource.class)
@ContextConfiguration(classes = {RestAuthenticationEntryPoint.class, SecurityConfig.class})
public class TestMealsResource {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private MealRepository repository;

    @MockBean
    private ApiUserDetailsService apiUserDetailsService;

    @Test
    public void testWithoutAuthentication() throws Exception {
        mvc.perform(get("/api/v1/meals")).
                andExpect(status().isUnauthorized());

        mvc.perform(get("/api/v1/meals/1")).
                andExpect(status().isUnauthorized());

        Meal meal = new Meal();
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.now());
        meal.setText("test_food");
        meal.setCalories(1000);
        ObjectMapper mapper = new ObjectMapper();
        String jsonLoad = mapper.writeValueAsString(meal);

        mvc.perform(post("/api/v1/meals").
                contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonLoad)).
                andExpect(status().isUnauthorized());

        mvc.perform(put("/api/v1/meals/1").
                contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonLoad)).
                andExpect(status().isUnauthorized());

        mvc.perform(delete("/api/v1/meals/1")).
                andExpect(status().isUnauthorized());
    }
}
