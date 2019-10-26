package service.calorie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 17:55
 * Purpose: TODO:
 **/
@EnableWebSecurity
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
