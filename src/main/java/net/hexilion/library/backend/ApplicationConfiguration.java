package net.hexilion.library.backend;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Main general configuration for the backend service.
 * Configuration class for loading properties from application.properties.
 */
@Configuration
public class ApplicationConfiguration {

    @Value("${api.key.header}")
    private @Nullable String apiKeyHeader;

    @Value("${api.key.path}")
    private @Nullable String apiKeyPath;

    /**
     * @return The API key header that must be used in requests to authenticate them.
     */
    public @Nullable String getApiKeyHeader() {
        return this.apiKeyHeader;
    }

    /**
     * This API key is used to validate the API keys in the incoming requests.
     * Therefore, this API is usually referred to as the "correct" API key.
     *
     * @return Path to where the correct API key is stored on the machine.
     */
    public @Nullable String getApiKeyPath() {
        return this.apiKeyPath;
    }
}