package service.calorie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 15:54
 * Purpose: TODO:
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Swagger-ui not works if we have @EnableWebMvc. We have to configure it here.
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}
