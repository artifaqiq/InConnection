package by.nc.lomako.security;

import by.nc.lomako.pojos.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String API_V1_AUTH_LOGIN_PATH = "/api/v1/auth/login";
    private static final String API_V1_AUTH_REGISTER_PATH = "/api/v1/auth/register";
    private static final String API_V1_AUTH_LOGOUT_PATH = "/api/v1/auth/logout";

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final UserDetailsService userDetailsService;

    private final LogoutSuccessHandler authLogoutHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public WebSecurityConfig(
            UserDetailsService userDetailsService,
            LogoutSuccessHandler authLogoutHandler,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.userDetailsService = userDetailsService;
        this.authLogoutHandler = authLogoutHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/v1/users/**", "/api/v1/posts/**", "/api/v1/messages/**")
                .hasAnyRole(RoleType.USER.name(), RoleType.MODERATOR.name(), RoleType.ADMIN.name())
                .antMatchers("/api/v1/admin/**")
                .hasRole(RoleType.ADMIN.name())
                .antMatchers(API_V1_AUTH_REGISTER_PATH, API_V1_AUTH_LOGIN_PATH)
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