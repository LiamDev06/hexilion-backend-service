package net.hexilion.library.backend.response;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response used for DELETE requests.
 */
public class DeleteRequestJsonResponse<T> extends AbstractBaseJsonResponse<T> {

    public DeleteRequestJsonResponse(@NonNull T data, @NonNull HttpServletResponse response) {
        super(data);

        // Set "Content Reset" status response
        response.setStatus(202);
    }

}