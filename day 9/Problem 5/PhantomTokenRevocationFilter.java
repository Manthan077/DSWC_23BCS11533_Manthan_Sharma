import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
class JwtBlacklistFilter
        extends OncePerRequestFilter {

    private final Set<String>
            blacklistedTokens;

    public JwtBlacklistFilter() {

        this.blacklistedTokens =
                new HashSet<>();

        blacklistedTokens.add(
                "revoked-token-example");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)

            throws ServletException,
            IOException {

        String authHeader =
                request.getHeader(
                        "Authorization");

        if (authHeader != null
                && authHeader.startsWith(
                "Bearer ")) {

            String token =
                    authHeader.substring(7);

            if (blacklistedTokens
                    .contains(token)) {

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Token revoked");

                return;
            }
        }

        filterChain.doFilter(
                request,
                response);
    }
}

@Configuration
class SecurityConfig {

    private final JwtBlacklistFilter
            jwtBlacklistFilter;

    private final GatekeeperStatelessAuthFilter
            jwtAuthenticationFilter;

    public SecurityConfig(
            JwtBlacklistFilter jwtBlacklistFilter,
            GatekeeperStatelessAuthFilter jwtAuthenticationFilter) {

        this.jwtBlacklistFilter =
                jwtBlacklistFilter;

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .addFilterBefore(
                        jwtBlacklistFilter,
                        GatekeeperStatelessAuthFilter.class)

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.
                                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

public class PhantomTokenRevocationFilter {

    public static void main(String[] args) {

        System.out.println(
                "Phantom Token Revocation Filter");
    }
}