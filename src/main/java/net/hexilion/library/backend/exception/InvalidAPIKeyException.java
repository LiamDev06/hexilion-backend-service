package net.hexilion.library.backend.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom exception class for representing an invalid API key authentication error.
 */
public class InvalidAPIKeyException extends AuthenticationException {

    public InvalidAPIKeyException(@NonNull String message) {
        super(message);
    }

}