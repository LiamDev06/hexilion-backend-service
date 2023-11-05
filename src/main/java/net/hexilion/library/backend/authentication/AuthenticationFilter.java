package net.hexilion.library.backend.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.exception.InvalidAPIKeyException;
import net.hexilion.library.backend.response.error.InvalidAPIKeyResponse;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Filter class for handling API authentication.
 */
public class AuthenticationFilter extends GenericFilterBean {

    private static final @NonNull String DEFAULT_INVALID_API_KEY_RESPONSE = "{\"success\": false, \"error\": \"Invalid API key\"}";

    private final @NonNull AuthenticationService authenticationService;

    public AuthenticationFilter(@NonNull AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            // Authentication
            Authentication authentication = this.authenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (InvalidAPIKeyException exception) {
            // Handle invalid API key
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Return invalid API key response
            String jsonResponse = this.constructInvalidAPIKeyResponse(httpResponse);
            httpResponse.getWriter().write(jsonResponse);
            return;
        }

        // Filter
        filterChain.doFilter(request, response);
    }

    private @NonNull String constructInvalidAPIKeyResponse(@NonNull HttpServletResponse response) {
        InvalidAPIKeyResponse<String> keyResponse = new InvalidAPIKeyResponse<String>(response);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(keyResponse);
        } catch (JsonProcessingException exception) {
            return DEFAULT_INVALID_API_KEY_RESPONSE;
        }
    }
}