package net.hexilion.library.backend.authentication;

import jakarta.servlet.http.HttpServletRequest;
import net.hexilion.library.backend.authentication.apikey.KeyAuthentication;
import net.hexilion.library.backend.authentication.apikey.CorrectKeyProvider;
import net.hexilion.library.backend.ApplicationConfiguration;
import net.hexilion.library.backend.exception.InvalidAPIKeyException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Service class for handling authentication checking and validation.
 * Compares the provided API key in the HTTP headers against the correct
 * API key stored securely on the machine of the backend service.
 */
public class AuthenticationService {

    private static final @NonNull String DEFAULT_API_KEY_HEADER = "API-Key";

    private final @NonNull ApplicationConfiguration configurationProperties;
    private final @NonNull CorrectKeyProvider correctKeyProvider;

    public AuthenticationService(@NonNull ApplicationConfiguration configurationProperties, @NonNull CorrectKeyProvider correctKeyProvider) {
        this.configurationProperties = configurationProperties;
        this.correctKeyProvider = correctKeyProvider;
    }

    public @NonNull Authentication getAuthentication(@NonNull HttpServletRequest request) {
        String apiKeyHeader = this.configurationProperties.getApiKeyHeader();
        if (apiKeyHeader == null) {
            apiKeyHeader = DEFAULT_API_KEY_HEADER;
        }

        // Get the API key from the request headers
        String apiKey = request.getHeader(apiKeyHeader);

        // Check if the API key is invalid
        if (apiKey == null || !apiKey.equals(this.correctKeyProvider.getCorrectApiKey())) {
            throw new InvalidAPIKeyException("Invalid API key provided");
        }

        // API is valid, return new authentication
        return new KeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}