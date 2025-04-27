package com.sin.management_system.infrastructures.configs;

import com.sin.management_system.infrastructures.dtos.LoginPageDto;
import com.sin.management_system.ui.auths.RegisterView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {
    private final JwtAuthFilterConfig jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final LoginPageDto loginPageDto;

    public static final String[] UN_SECRET_URLS = {
            "/login", "/logout", "/oauth2/**", "/register", "/VAADIN/**","/images/**",
    };

    public static final String[] OAUTH_URLS = {
            "/oauth/**"
    };

//    @Bean
//    @Order(1)
//    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher(OAUTH_URLS)
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll())
////                .csrf(AbstractHttpConfigurer::disable)
////                .oauth2Login(oauth2Login -> oauth2Login
////                        .loginPage(loginPageDto.getLoginPage())
////                        .defaultSuccessUrl(loginPageDto.getDefaultPage())
////                        .failureUrl(loginPageDto.getFailurePage()))
//                .oauth2Login(Customizer.withDefaults())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//        return http.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain vaadinSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/**")

    /// /                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(UN_SECRET_URLS).permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//                .authenticationProvider(authProvider)
//                .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(UN_SECRET_URLS).permitAll())
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        super.configure(http);
    }
}
