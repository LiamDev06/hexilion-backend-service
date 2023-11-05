package net.hexilion.library.backend.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.hexilion.library.backend.credentials.MongoCredentials;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import net.hexilion.library.backend.exception.MongoInstanceException;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.logging.Logger;

/**
 * Represents an instace of a MongoDB connection.
 */
public class MongoInstance {

    private final @NonNull String identifier;
    private final @NonNull MongoInstanceType instanceType;
    private final MongoClient client;

    public MongoInstance(@NonNull MongoInstanceType instanceType, @NonNull MongoCredentials credentials) throws MongoInstanceException {
        // Log start
        long start = System.currentTimeMillis();

        Logger log = Logger.getLogger("mongo");
        this.instanceType = instanceType;
        this.identifier = credentials.identifier();

        log.info(String.format("Hooking into Mongo instance with type %s and identifier %s...",
                instanceType.getDisplayName(), identifier));

        // Validate Mongo credentials
        this.validateCredentials(credentials);

        // Create the connection string
        final String connectUri = String.format("mongodb://%s:%s@%s:%s/%s",
                credentials.user(),
                credentials.password(),
                credentials.host(),
                credentials.port(),
                "admin");
        ConnectionString connectionString = new ConnectionString(connectUri);

        try {
            // Build the client settings
            MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applicationName("HexLib Mongo Instance")
                    .applyConnectionString(connectionString)
                    .build();

            // Create the client connection
            this.client = MongoClients.create(clientSettings);
        } catch (Exception exception) {
            throw new MongoInstanceException("Could not connect to the Mongo client!");
        }

        // Creation done
        long end = System.currentTimeMillis();
        log.info(String.format("Finished hooking into Mongo instance %s which took %dms!",
                this.identifier, end - start));
    }

    private void validateCredentials(@NonNull MongoCredentials credentials) throws MongoInstanceException {
        // Identifier is invalid
        if (credentials.identifier().equals("")) {
            throw new MongoInstanceException("The identifier value is empty and therefore not valid!");
        }

        // IP is invalid
        if (credentials.host().equals("")) {
            throw new MongoInstanceException("The IP value is empty and therefore not valid!");
        }

        // Port is invalid
        if (credentials.port() <= 0) {
            throw new MongoInstanceException("The port cannot be less than or equal to 0!");
        }

        // User is invalid
        if (credentials.user().equals("")) {
            throw new MongoInstanceException("The user value is empty and therefore not valid!");
        }

        // The password is either empty or does not meet length requirements
        final String password = credentials.password();
        if (password.length() < 21) {
            throw new MongoInstanceException("The password is either empty or did not meet the length requirements!");
        }
    }

    public void shutdown() {
        // Close the client connection if it exists
        if (this.client != null) {
            this.client.close();
        }
    }

    public @Nullable MongoCollection<Document> getCollectionByIdentifier(@NonNull String database, @NonNull String collection) {
        // Get the database from the client
        MongoDatabase foundDatabase = this.client.getDatabase(database);
        if (foundDatabase == null) {
            return null;
        }

        // Return the collection
        return foundDatabase.getCollection(collection);
    }

    /**
     * @return The custom identifier of the instance.
     */
    public @NonNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * @return The Mongo instance type of this instance.
     */
    public @NonNull MongoInstanceType getInstanceType() {
        return this.instanceType;
    }
}