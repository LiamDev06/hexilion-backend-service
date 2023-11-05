package net.hexilion.library.backend.database.mongo.enums;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents different types of Mongo instances.
 */
public enum MongoInstanceType {

    MASTER("Master"),
    NETWORK("Network"),
    GAME("Game");

    public static final @NonNull MongoInstanceType[] VALUES = MongoInstanceType.values();

    private final @NonNull String displayName;

    MongoInstanceType(@NonNull String displayName) {
        this.displayName = displayName;
    }

    public @NonNull String getDisplayName() {
        return this.displayName;
    }

}