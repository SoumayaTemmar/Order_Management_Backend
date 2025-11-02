package com.soumaya.orders_management_app.backend.Security;

import com.soumaya.orders_management_app.backend.ExceptionHandling.CustomAccessDeniedHandler;
import com.soumaya.orders_management_app.backend.ExceptionHandling.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( req ->
                        req.requestMatchers(
                                    "api/v1/auth/login",
                                    "api/v1/user/password/update",
                                    "api/v1/products/all",
                                    "uploads/**").permitAll()
                            .requestMatchers(
                                    "api/v1/auth/register",
                                    "api/v1/user/**",
                                    "api/v1/products/**"
                                    ).hasRole("ADMIN")
                            .requestMatchers(
                                    "api/v1/cmd/**"
                                    ).hasAnyRole("ADMIN", "RESPONSABLE_CMD")
                            .requestMatchers(
                                    "api/v1/orders/**"
                            ).hasAnyRole("ADMIN", "VENDEUR")
                            .anyRequest().authenticated()
                )
                .exceptionHandling(ex->
                        ex.accessDeniedHandler(customAccessDeniedHandler)
                          .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
