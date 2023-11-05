package net.hexilion.library.backend.database.mongo;

import net.hexilion.library.backend.credentials.MongoCredentials;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import net.hexilion.library.backend.exception.MongoInstanceException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages the Mongo instances in the application.
 */
public class MongoDataManager {

    private final @NonNull Map<MongoInstanceType, MongoInstance> instances;
    private final @NonNull Environment environment;

    public MongoDataManager(@NonNull Logger log, @NonNull Environment environment) {
        // Initialize
        this.instances = new HashMap<>();
        this.environment = environment;

        // Load in all Mongo instances
        for (MongoInstanceType type : MongoInstanceType.VALUES) {
            MongoInstance instance = this.createMongoInstance(type, log);

            // Something went wrong, instance is null
            if (instance == null) {
                log.severe("The Mongo instance with type " + type.name() + " could not be loaded in!");
                continue;
            }

            this.instances.putIfAbsent(type, instance);
        }

        // Complete
        int size = this.instances.size();
        if (size == 0) {
            log.severe("No Mongo instances were loaded in!");
            return;
        }

        log.info("Loaded in " + size + " Mongo instances!");
    }

    private @Nullable MongoInstance createMongoInstance(@NonNull MongoInstanceType type, @NonNull Logger log) {
        // Load in all the credentials
        final String path = "mongo." + type.name().toLowerCase() + ".";
        MongoCredentials credentials = new MongoCredentials(
                this.environment.getProperty(path + "identifier", ""),
                this.environment.getProperty(path + "host", ""),
                this.environment.getProperty(path + "port", Integer.class, 0),
                this.environment.getProperty(path + "user", ""),
                this.environment.getProperty(path + "password", "")
        );

        // Try creating a new Mongo instance
        try {
            return new MongoInstance(type, credentials);
        } catch (MongoInstanceException exception) {
            log.severe("Could not load Mongo instance type with identifier " + credentials.identifier() + "!");
        }

        return null;
    }

    public MongoInstance getInstanceByType(@NonNull MongoInstanceType type) {
        return this.instances.get(type);
    }
}