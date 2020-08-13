package com.freightmate.harbour.configuration;

import com.freightmate.harbour.filter.JWTAuthorizationFilter;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

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
            // disable cross site request forgery, as we don't use cookies - otherwise ALL PUT, POST, DELETE will get HTTP 403!
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
            .and()
                .csrf().disable();

    }
}
