package net.hexilion.library.backend.response;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response used for POST requests.
 */
public class PostRequestJsonResponse<T> extends AbstractBaseJsonResponse<T> {

    public PostRequestJsonResponse(@NonNull T data, @NonNull HttpServletResponse response) {
        super(data);

        // Set "Created OK" status response
        response.setStatus(201);
    }

}