import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.
        SecurityContextHolder;

import org.springframework.security.core.userdetails.
        User;

import org.springframework.security.web.authentication.
        WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.
        OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class GatekeeperStatelessAuthFilter
        extends OncePerRequestFilter {

    private final FortressJwtMintingEngine
            jwtProvider;

    public GatekeeperStatelessAuthFilter(
            FortressJwtMintingEngine jwtProvider) {

        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)

            throws ServletException,
            IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader != null
                && authHeader.startsWith("Bearer ")) {

            String token =
                    authHeader.substring(7);

            try {

                String username =
                        jwtProvider.extractUsername(
                                token);

                if (username != null
                        && SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        == null) {

                    User user =
                            new User(
                                    username,
                                    "",
                                    Collections.emptyList());

                    UsernamePasswordAuthenticationToken
                            authentication =

                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities());

                    authentication.setDetails(

                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication);
                }

            } catch (Exception ex) {

                System.out.println(
                        "JWT Validation Failed : "
                                + ex.getMessage());
            }
        }

        filterChain.doFilter(
                request,
                response);
    }
}