package com.vcarrin87.jdbi_example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.vcarrin87.jdbi_example.exceptions.CustomAccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable())
            .authorizeHttpRequests((requests) -> requests
            .requestMatchers(org.springframework.http.HttpMethod.GET, "/**").authenticated()
            .requestMatchers(org.springframework.http.HttpMethod.POST, "/**").hasAuthority("admin")
            .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/**").hasAuthority("admin")
            .requestMatchers("/error", "/register", "/login/**").permitAll()
            );
        http.formLogin(form -> form
            .defaultSuccessUrl("/swagger-ui/index.html", true)
        );
        http
            .httpBasic(withDefaults())
            .exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
