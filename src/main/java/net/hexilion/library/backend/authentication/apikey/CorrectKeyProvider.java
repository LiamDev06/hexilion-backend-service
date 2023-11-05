package net.hexilion.library.backend.authentication.apikey;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the correct API key that is used to validate the API key in
 * the response headers from all requests.
 */
public class CorrectKeyProvider {

    private final @NonNull String apiKey;

    public CorrectKeyProvider(@NonNull String apiKeyPath) {
        this.apiKey = this.readApiKeyFromFile(apiKeyPath);
    }

    /**
     * Reads the API key from the specified file path.
     *
     * @param apiKeyPath The path to the file containing the API key.
     * @return The API key as a string.
     */
    private String readApiKeyFromFile(@NonNull String apiKeyPath) {
        try {
            Path filePath = Paths.get(apiKeyPath);
            return Files.readString(filePath).trim();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read API key file: " + apiKeyPath, exception);
        }
    }

    /**
     * @return The correct API key used to validate API keys in incoming requests.
     */
    public @NonNull String getCorrectApiKey() {
        return this.apiKey;
    }
}