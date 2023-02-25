package kr.api.lenders.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable() // disable csrf because we are using jwt
                .authorizeHttpRequests()
                .requestMatchers("/auth/**") // open all auth endpoints
                .permitAll()
                .anyRequest()
                .authenticated();
        /**
         * [TODO]
         *   add jwt filter
         */

        return http.build();
    }
}
