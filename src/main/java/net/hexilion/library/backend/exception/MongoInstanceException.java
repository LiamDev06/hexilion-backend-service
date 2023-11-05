package net.hexilion.library.backend.exception;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Exception for when something goes wrong when trying to establish a connection
 * to a Mongo instance.
 */
public class MongoInstanceException extends Exception {

    public MongoInstanceException(@NonNull String message) {
        super(message);
    }

}