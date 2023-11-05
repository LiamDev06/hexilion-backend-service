package net.hexilion.library.backend.response.error;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response for indicating that the requested data was not found.
 */
public class DataNotFoundResponse<T> extends AbstractBaseJsonResponse<T> {

    public DataNotFoundResponse(@NonNull String collection, @NonNull String identifier, @NonNull HttpServletResponse response) {
        super(String.format("The JSON data document with identifier '%s' was not found in the collection '%s'.", identifier, collection));

        // Set "Not Found" status
        this.applyStatusCode(response);
    }

    public DataNotFoundResponse(@NonNull String collection, @NonNull HttpServletResponse response) {
        super(String.format("The collection with identifier '%s' was not found.", collection));

        // Set "Not Found" status
        this.applyStatusCode(response);
    }

    public DataNotFoundResponse(@NonNull HttpServletResponse response, @NonNull String customErrorMessage) {
        super(customErrorMessage);

        // Set "Not Found" status
        this.applyStatusCode(response);
    }

    private void applyStatusCode(@NonNull HttpServletResponse response) {
        response.setStatus(404);
    }

}