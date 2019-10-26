package service.calorie.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 22:55
 * Purpose: TODO:
 **/
@RestController
public class TestResource {

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
