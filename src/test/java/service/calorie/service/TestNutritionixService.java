package service.calorie.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 23:57
 * Purpose: Test
 **/
@RunWith(SpringRunner.class)
public class TestNutritionixService {

    @TestConfiguration
    // Without static it was not working.
    static class TestConfig {

        @Bean
        public NutritionixService service(RestTemplate template) {
            return new NutritionixService(template);
        }
    }

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private NutritionixService service;

    @Test
    public void testFetchCalorieFromText() {
        String payload = "{\"foods\":[{\"nf_calories\":10},{\"nf_calories\":20},{\"nf_calories\":30}," +
                "{\"nf_calories\":40}]}";
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any())).
                thenReturn(payload);
        assert service.fetchCaloriesFromText("1 apple") == 100;
    }

    @Test
    public void testFetchCalorieFromTextKeyFoodsIsNotPresent() {
        String payload = "{\"xxx\":[{\"nf_calories\":10},{\"nf_calories\":20},{\"nf_calories\":30}," +
                "{\"nf_calories\":40}]}";
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any())).
                thenReturn(payload);
        assert service.fetchCaloriesFromText("1 apple") == 0;
    }

    @Test
    public void testFetchCalorieFromTextKeyNF_CaloriesNotPresent() {
        String payload = "{\"foods\":[{\"xxx\":10},{\"nf_calories\":20},{\"nf_calories\":30}," +
                "{\"nf_calories\":40}]}";
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any())).
                thenReturn(payload);
        assert service.fetchCaloriesFromText("1 apple") == 0;
    }

    @Test
    public void testFetchCalorieFromTextKeyNF_CaloriesIsNotInt() {
        String payload = "{\"foods\":[{\"nf_calories\":\"xxx\"},{\"nf_calories\":20},{\"nf_calories\":30}," +
                "{\"nf_calories\":40}]}";
        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any())).
                thenReturn(payload);
        assert service.fetchCaloriesFromText("1 apple") == 90;
    }

}
