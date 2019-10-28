package service.calorie.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import service.calorie.util.Constants;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 23:05
 * Purpose: Configuration which is not general enough to deserve its own class.
 **/
@Configuration
public class CommonConfig {

    /**
     * For swagger configuration
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constants.SWAGGER_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Rest client to access third party nutritionix APIs.
     *
     * @param builder
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
