package net.hexilion.library.backend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import net.hexilion.library.backend.response.base.FetchedFrom;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Response used for GET requests.
 */
public class GetRequestJsonResponse<T> extends AbstractBaseJsonResponse<T> {

    private final @NonNull @JsonProperty("from") FetchedFrom fetchedFrom;

    public GetRequestJsonResponse(@NonNull T data, @NonNull FetchedFrom fetchedFrom, @NonNull HttpServletResponse response) {
        super(data);

        this.fetchedFrom = fetchedFrom;

        // Set "OK" status response
        response.setStatus(200);
    }

    public @NonNull FetchedFrom getFetchedFrom() {
        return this.fetchedFrom;
    }
}