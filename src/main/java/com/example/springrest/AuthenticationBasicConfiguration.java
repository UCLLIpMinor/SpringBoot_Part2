package com.example.springrest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthenticationBasicConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("elste")
                .password(encoder.encode("t"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/add").hasRole("ADMIN")
                .antMatchers("/*").hasRole("USER")
                // ADDED FOR H2 ACCESS VIA BROWSER
                .antMatchers("/console/**").permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                // ADDED FOR CSFR (post add not working problem)
                // disable csrf (needed for rest api)
                // use th:action in add-patient.html to use Thymeleaf CSRF processor
                .csrf().disable()
                .httpBasic();
    }
}