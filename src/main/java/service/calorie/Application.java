package service.calorie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 17:55
 * Purpose: Entry point of the application
 **/
@EnableSwagger2
@EnableWebSecurity
@SpringBootApplication()
@PropertySource("classpath:nutritionix_service.properties")
public class Application {

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
