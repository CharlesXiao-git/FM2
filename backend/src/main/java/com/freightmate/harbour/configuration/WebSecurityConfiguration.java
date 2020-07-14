package com.freightmate.harbour.configuration;

import com.freightmate.harbour.filter.JWTAuthorizationFilter;
import com.freightmate.harbour.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Make sure we can provide method level auth as well
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic() // Enable Spring HTTP Basic Auth, this enables security middleware
            .and()
                .authorizeRequests()
                    .antMatchers("/","/login","/api/v1/auth/login").permitAll() // Allow anyone to access root,login page and login api
                    .antMatchers("/api/v1/admin/**").hasRole("ADMIN") // only admins can his the admin URLs
                    .antMatchers("/api/v1/broker/**").hasRole("BROKER") // only brokers can his the broker URLs
                    .antMatchers("/static/**").permitAll() // serve static assets
                    .anyRequest().authenticated() // any request not specified above requires user to be logged in
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(), this.authService))
                // disable cross site request forgery, as we don't use cookies - otherwise ALL PUT, POST, DELETE will get HTTP 403!
                .csrf().disable();

    }
}
