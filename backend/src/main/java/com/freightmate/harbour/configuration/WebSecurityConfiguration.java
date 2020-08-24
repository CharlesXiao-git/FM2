package com.freightmate.harbour.configuration;

import com.freightmate.harbour.filter.JWTAuthorizationFilter;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Make sure we can provide method level auth as well
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    private static final String API_LOGIN = "/api/v1/auth/login";
    private static final String ADMIN_SUBROUTES = "/api/v1/admin/**";
    private static final String BROKER_SUBROUTES = "/api/v1/broker/**";
    private static final String STATIC_SUBROUTEES = "/static/**";
    private static final String HEALTHCHECK_ENDPOINT = "/actuator/health";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("OPTIONS", "GET", "HEAD", "POST", "PATCH" ,"PUT", "DELETE"));
        configuration.addExposedHeader("Authorization");
        configuration.setMaxAge(Duration.ofDays(1));

        http
            .authorizeRequests()
                .antMatchers(API_LOGIN,HEALTHCHECK_ENDPOINT).permitAll() // Allow anyone to access root,login page and login api
                .antMatchers(ADMIN_SUBROUTES).hasRole(UserRole.ADMIN.name()) // only admins can his the admin URLs
                .antMatchers(BROKER_SUBROUTES).hasRole(UserRole.BROKER.name()) // only brokers can his the broker URLs
                .antMatchers(STATIC_SUBROUTEES).permitAll() // serve static assets
                .anyRequest().authenticated() // any request not specified above requires user to be logged in
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), this.authService))
                .cors()
            .and()
            // disable cross site request forgery, as we don't use cookies - otherwise ALL PUT, POST, DELETE will get HTTP 403!
                .csrf().disable();

    }

    @Configuration
    @EnableWebMvc
    public static class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("OPTIONS", "GET", "HEAD", "POST", "PATCH" ,"PUT", "DELETE")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true).maxAge(3600);
        }
    }

}
