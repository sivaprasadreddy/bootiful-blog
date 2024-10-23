package com.sivalabs.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String[] PUBLIC_RESOURCES = {
        "/", "/favicon.ico", "/actuator/**", "/error", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/assets/**",
    };

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**");
        http.csrf(CsrfConfigurer::disable);
        http.cors(CorsConfigurer::disable);

        http.authorizeHttpRequests(c -> c.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers("/api/login")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/posts/*/comments")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()));
        http.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        String[] unsecuredPaths = {
            "/", "/login", "/register",
        };
        http.securityMatcher("/**");

        http.csrf(CsrfConfigurer::disable);
        http.authorizeHttpRequests(r -> r.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(unsecuredPaths)
                .permitAll()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/blog/posts", "/blog/posts/{slug}")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/blog/posts/*/comments")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.formLogin(formLogin -> formLogin.loginPage("/login").permitAll());

        http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll());

        return http.build();
    }
}
