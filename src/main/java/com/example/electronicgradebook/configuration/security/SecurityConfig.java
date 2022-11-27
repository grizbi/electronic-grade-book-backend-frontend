package com.example.electronicgradebook.configuration.security;

import com.example.electronicgradebook.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.electronicgradebook.util.Values.ADMIN_AUTHORITY;
import static com.example.electronicgradebook.util.Values.USER_AUTHORITY;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/students", "/students-total", "/total-marks", "/special-students").hasAuthority(ADMIN_AUTHORITY)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/users/**").hasAuthority(ADMIN_AUTHORITY)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(ADMIN_AUTHORITY)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/highest-grade", "/average-class-grade").hasAuthority(USER_AUTHORITY)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
