package net.hexilion.library.backend.response.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents information fetched from a database and its collection.
 * This is used in JSON responses.
 */
public record FetchedFrom(@NonNull String database, @NonNull String collection) {

    /**
     * Concatenates the database and collection into a single string representation.
     *
     * @return The full name of the database and collection.
     */
    @JsonIgnore
    public @NonNull String getOutput() {
        return this.database + ":" + this.collection;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return this.getOutput();
    }
}