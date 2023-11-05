package net.hexilion.library.backend.credentials;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents the credentials required to connect to a Mongo instance.
 *
 * @param identifier The identifier of the database, the naming/id. This is not used when establishing a connecting.
 */
public record MongoCredentials(@NonNull String identifier, @NonNull String host, int port,
                               @NonNull String user, @NonNull String password)  { }