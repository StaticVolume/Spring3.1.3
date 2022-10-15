package com.SpringSecurity.security.sources.config;

import com.SpringSecurity.security.sources.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;

    @Autowired
    public SecurityConfig(UserServiceImpl userService) {

        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        userService.setPasswordEncoder(getPasswordEncoder());
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(userService.getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user","/login","/logout").hasAnyRole("USER", "ADMIN")
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
