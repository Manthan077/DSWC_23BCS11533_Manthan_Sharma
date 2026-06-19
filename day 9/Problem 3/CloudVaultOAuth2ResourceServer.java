import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CloudVaultOAuth2ResourceServer {

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                .csrf(csrf ->
                        csrf.disable())

                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(
                                        "/api/v1/public/**")
                                .permitAll()

                                .requestMatchers(
                                        "/api/v1/admin/**")
                                .hasAuthority(
                                        "SCOPE_admin")

                                .anyRequest()
                                .authenticated())

                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt -> {
                                        }));

        return http.build();
    }
}