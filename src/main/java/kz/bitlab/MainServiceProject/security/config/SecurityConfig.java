package kz.bitlab.MainServiceProject.security.config;

import kz.bitlab.MainServiceProject.security.util.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.
                                requestMatchers("/user/create").hasAnyAuthority("ROLE_ADMIN").

                                requestMatchers("/course/save").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/course/courses/").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/course/delete/").hasAnyAuthority("ROLE_ADMIN").

                                requestMatchers("/chapter/save").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/chapter/update/").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/chapter/delete/").hasAnyAuthority("ROLE_ADMIN").

                                requestMatchers("/lesson/save").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/lesson/update/").hasAnyAuthority("ROLE_ADMIN").
                                requestMatchers("/lesson/delete/").hasAnyAuthority("ROLE_ADMIN").

                                requestMatchers("/user/update").permitAll().
                                requestMatchers("/user/sign-in").permitAll().
                                requestMatchers("/user/refresh").permitAll().
                                requestMatchers("/user/set-role").hasAnyAuthority("ROLE_ADMIN").
                                anyRequest().authenticated()
                )
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(o -> o
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(keycloakRoleConverter())));
        return httpSecurity.build();
    }

    @Bean
    public KeycloakRoleConverter keycloakRoleConverter() {
        return new KeycloakRoleConverter();
    }
}
