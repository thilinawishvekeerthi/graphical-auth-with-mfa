package com.graphicauth.authservice.security;

import com.graphicauth.authservice.service.IGraphicAuthService;
import com.graphicauth.authservice.service.IJwtAuthManagerService;
import com.graphicauth.authservice.service.ITotpAuthService;
import com.graphicauth.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtConfiguration jwtConfiguration;
    private final IJwtAuthManagerService jwtAuthManagerService;
    private final ITotpAuthService totpAuthService;
    private final IGraphicAuthService graphicAuthService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomUserNamePasswordAuthenticationFilter customAuthenticationFilter = new CustomUserNamePasswordAuthenticationFilter(authenticationManagerBean(), userService, jwtConfiguration, totpAuthService, graphicAuthService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        String[] swaggerPaths = new String[]{"/swagger-ui.html","/swagger-ui/**", "/v3/api-docs/**" };
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(swaggerPaths).permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/login/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/auth/sign-up/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/auth/totp/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/image/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/user/config/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthPerRequestFilter(jwtConfiguration, jwtAuthManagerService), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
}
