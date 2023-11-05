package net.hexilion.library.backend.response.error;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response for when an HTTP request is missing parameters.
 */
public class MissingParameterResponse<T> extends AbstractBaseJsonResponse<T> {

    public MissingParameterResponse(@NonNull HttpServletResponse response) {
        super("You are missing required parameters for this response to work properly.");

        // Set "Bad Request" status
        response.setStatus(400);
    }

}