package net.hexilion.library.backend.response.error;

import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response for when something goes wrong or an invalid action is performed
 * regarding a JSON database document.
 */
public class InvalidJsonDocumentActionResponse<T> extends AbstractBaseJsonResponse<T> {

    public InvalidJsonDocumentActionResponse(@NonNull String errorCause, @NonNull HttpServletResponse response) {
        super(errorCause);

        // Set "Bad Request" status
        response.setStatus(400);
    }

}