import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CleanFailSecurityExceptionHandler {

    public static void main(String[] args) {

        System.out.println(
                "Clean-Fail Security Exception Handler");
    }
}

class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException)

            throws IOException,
            ServletException {

        response.setContentType(
                "application/json");

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                "Unauthorized");

        error.put(
                "message",
                authException.getMessage());

        objectMapper.writeValue(
                response.getOutputStream(),
                error);
    }
}

class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)

            throws IOException {

        response.setContentType(
                "application/json");

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN);

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                "Forbidden");

        error.put(
                "message",
                accessDeniedException.getMessage());

        objectMapper.writeValue(
                response.getOutputStream(),
                error);
    }
}