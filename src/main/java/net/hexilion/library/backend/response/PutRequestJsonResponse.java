package net.hexilion.library.backend.response;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response used for PUT requests.
 */
public class PutRequestJsonResponse<T> extends AbstractBaseJsonResponse<T> {

    public PutRequestJsonResponse(@NonNull T data, @NonNull HttpServletResponse response) {
        super(data);

        // Set "Success" status response
        response.setStatus(200);
    }

}