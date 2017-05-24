package by.nc.lomako.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String API_V1_AUTH_LOGIN_PATH = "/login";
    private static final String API_V1_AUTH_REGISTER_PATH = "/api/v1/auth/register";
    private static final String API_V1_AUTH_LOGOUT_PATH = "/api/v1/auth/logout";
    private static final String USERNAME_PARAMETER = "email";
    private static final String PASSWORD_PARAMETER = "password";

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final UserDetailsService userDetailsService;

    private final AuthenticationSuccessHandler authSuccessHandler;

    private final AuthenticationFailureHandler authFailureHandler;

    private final LogoutSuccessHandler authLogoutHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public WebSecurityConfig(
            UserDetailsService userDetailsService, AuthenticationSuccessHandler authSuccessHandler,
            AuthenticationFailureHandler authFailureHandler, LogoutSuccessHandler authLogoutHandler,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.userDetailsService = userDetailsService;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
        this.authLogoutHandler = authLogoutHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(API_V1_AUTH_REGISTER_PATH, API_V1_AUTH_LOGIN_PATH).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                .loginProcessingUrl(API_V1_AUTH_LOGIN_PATH)
                .usernameParameter(USERNAME_PARAMETER)
                .passwordParameter(PASSWORD_PARAMETER)
                .failureHandler(authFailureHandler)
                .successHandler(authSuccessHandler)
                    .permitAll()
                .and()
                    .logout()
                .logoutUrl(API_V1_AUTH_LOGOUT_PATH)
                .logoutSuccessHandler(authLogoutHandler)
                    .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

    }


}