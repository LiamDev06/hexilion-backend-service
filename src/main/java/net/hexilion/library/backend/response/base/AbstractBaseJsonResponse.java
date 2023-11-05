package net.hexilion.library.backend.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Abstract base class for JSON responses.
 * Provides common properties and methods for JSON response objects.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractBaseJsonResponse<T> {

    private final boolean success;
    private final @Nullable @JsonProperty("cause") String errorCause;
    private @Nullable @JsonProperty("data") T data;

    public AbstractBaseJsonResponse(@NonNull T data) {
        this.success = true;
        this.errorCause = null;
        this.data = data;
    }

    public AbstractBaseJsonResponse(@NonNull String errorCause) {
        this.success = false;
        this.errorCause = errorCause;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public @Nullable String getErrorCause() {
        return this.errorCause;
    }

    public @Nullable T getData() {
        return this.data;
    }
}