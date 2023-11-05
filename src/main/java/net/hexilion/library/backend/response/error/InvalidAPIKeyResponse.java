package net.hexilion.library.backend.response.error;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response for when the request has an invalid API key.
 */
public class InvalidAPIKeyResponse<T> extends AbstractBaseJsonResponse<T> {

    public InvalidAPIKeyResponse(@NonNull HttpServletResponse response) {
        super("Invalid API key.");

        // Set "Unauthorized" status
        response.setStatus(401);
    }

}