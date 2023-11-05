package net.hexilion.library.backend.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom exception class for representing that the path where the API key
 * is located on the machine running the application, is invalid.
 * <p>
 * This could mean that the path is null, does not exist or is provided wrong.
 */
public class InvalidAPIKeyPathException extends AuthenticationException {

    public InvalidAPIKeyPathException(@NonNull String path, @NonNull String message) {
        super(String.format("Invalid API key path: '%s'. %s", path, message));
    }

}