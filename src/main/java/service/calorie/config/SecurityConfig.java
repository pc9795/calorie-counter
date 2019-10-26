package service.calorie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import service.calorie.service.ApiUrlAuthenticationSuccessHandler;
import service.calorie.service.ApiUserDetailsService;
import service.calorie.service.RestAuthenticationEntryPoint;
import service.calorie.util.Constants;

import javax.sql.DataSource;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 19:33
 * Purpose: TODO:
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final ApiUserDetailsService service;
    private final ApiUrlAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationEntryPoint entryPoint;

    @Autowired
    public SecurityConfig(DataSource dataSource, ApiUserDetailsService service
            , ApiUrlAuthenticationSuccessHandler successHandler, RestAuthenticationEntryPoint entryPoint) {
        this.dataSource = dataSource;
        this.service = service;
        this.successHandler = successHandler;
        this.entryPoint = entryPoint;
    }

    /**
     * Enabling jdbc authentication.
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // We could have used 'defaultSchema' but it won't work with postgresql and mysql.
        auth.userDetailsService(service).passwordEncoder(encoder()).
                and().
                jdbcAuthentication().dataSource(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                // Custom handling on authentication failures
                exceptionHandling().authenticationEntryPoint(entryPoint).
                and().
                // Authorization
                authorizeRequests().
                antMatchers(Constants.API_V1_URL + "/test").authenticated().
                and().
                formLogin().
                successHandler(successHandler).
                failureHandler(new SimpleUrlAuthenticationFailureHandler()).
                and().
                logout();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
