package com.example.springrest.generic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean // Also overrides default spring boot password-encoder
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Leaks passwords to github repository and git history!
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("t"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean // Turn off security for certain URLs
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/api/**")
                .requestMatchers(antMatcher("/h2/**")); // https://stackoverflow.com/questions/74680244/h2-database-console-not-opening-with-spring-security;
    }

    @Bean // Configure security (authorization) for all URLs for which it is enabled
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http // Order matters!!
            .authorizeHttpRequests()
            .requestMatchers("/home").permitAll()
            .requestMatchers("/add").hasRole("ADMIN")
            .anyRequest().hasRole("USER")
            .and()
            .headers().frameOptions().disable()
            .and()
            .httpBasic();

        return http.build();
    }
}