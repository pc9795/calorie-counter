package service.calorie.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 20:28
 * Purpose: TODO:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestNutritionixService {
    @Autowired
    private NutritionixService service;

    @Test
    public void testFetchCaloriesFromText() throws IOException {
        assert service.fetchCaloriesFromText("1 apple") == 94;
    }
}
