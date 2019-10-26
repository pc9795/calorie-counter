package service.calorie.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.calorie.util.Constants;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 22:55
 * Purpose: TODO:
 **/
@RestController()
@RequestMapping(Constants.API_V1_URL + "/test")
public class TestResource {

    @GetMapping()
    public String test() {
        return "test";
    }
}
