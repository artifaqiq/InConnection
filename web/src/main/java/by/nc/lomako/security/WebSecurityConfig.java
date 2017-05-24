package by.nc.lomako.security;

import by.nc.lomako.security.handlers.AuthFailureHandler;
import by.nc.lomako.security.handlers.AuthLogoutHandler;
import by.nc.lomako.security.handlers.AuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final UserDetailsService userDetailsService;

    private final AuthSuccessHandler authSuccessHandler;

    private final AuthFailureHandler authFailureHandler;

    private final AuthLogoutHandler authLogoutHandler;

    public WebSecurityConfig(UserDetailsService userDetailsService, AuthSuccessHandler authSuccessHandler,
                             AuthFailureHandler authFailureHandler, AuthLogoutHandler authLogoutHandler) {
        this.userDetailsService = userDetailsService;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
        this.authLogoutHandler = authLogoutHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                    .permitAll()
                .and()
                    .logout()
                .logoutSuccessHandler(authLogoutHandler)
                    .permitAll()
                .and()
                    .csrf()
                    .disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

    }


}