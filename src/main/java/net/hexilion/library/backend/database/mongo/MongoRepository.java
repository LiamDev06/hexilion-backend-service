package net.hexilion.library.backend.database.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.hexilion.library.backend.database.mongo.enums.JsonDocumentActionResult;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to more easily get data from Mongo databases and collections.
 */
public abstract class MongoRepository {

    private static final @NonNull String DOCUMENT_IDENTIFIER = "_id";

    public final @NonNull MongoInstanceType type;
    private final @NonNull MongoInstance instance;

    public MongoRepository(@NonNull MongoInstanceType type, @NonNull MongoDataManager dataManager) {
        this.type = type;
        this.instance = dataManager.getInstanceByType(type);
    }

    /**
     * "Create" a new document by inserting a JSON document into a Mongo repository.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to create the document in.
     * @param documentId The identifier '_id' value of the document being created.
     * @param document The actual document to insert into the collection.
     * @return Enum result of the return of the creating.
     */
    public JsonDocumentActionResult createDocument(@NonNull String database, @NonNull String collection, @NonNull String documentId, @NonNull Document document) {
        // Get the collection from the database
        MongoCollection<Document> foundCollection = this.instance.getCollectionByIdentifier(database, collection);
        if (foundCollection == null) {
            return JsonDocumentActionResult.NO_COLLECTION;
        }

        // Check if a document with the same identifier already exists
        Document foundDocument = foundCollection.find(Filters.eq(DOCUMENT_IDENTIFIER, documentId)).first();
        if (foundDocument != null) {
            return JsonDocumentActionResult.ALREADY_EXISTS;
        }

        // Insert the new document
        foundCollection.insertOne(document);
        return JsonDocumentActionResult.SUCCESS;
    }

    /**
     * @see MongoRepository#updateDocument(String, String, Bson, Map)
     */
    public JsonDocumentActionResult updateDocument(@NonNull String database, @NonNull String collection, @NonNull String documentId, @NonNull Map<String, Object> updateContent) {
        return this.updateDocument(database, collection, Filters.eq(DOCUMENT_IDENTIFIER, documentId), updateContent);
    }

    /**
     * Update the contents in a JSON document.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to update the document in.
     * @param filter Bson filter used to find the target document to update.
     * @param updateContent Map containing the keys and their values to update. This can include both new fields to append but also existing ones to replace.
     * @return Enum result of the return of the updating.
     */
    public JsonDocumentActionResult updateDocument(@NonNull String database, @NonNull String collection, @NonNull Bson filter, @NonNull Map<String, Object> updateContent) {
        // Get the collection from the database
        MongoCollection<Document> foundCollection = this.instance.getCollectionByIdentifier(database, collection);
        if (foundCollection == null) {
            return JsonDocumentActionResult.NO_COLLECTION;
        }

        // Check if a document exists with the update identifier
        Document foundDocument = foundCollection.find(filter).first();
        if (foundDocument == null) {
            return JsonDocumentActionResult.NO_EXIST;
        }

        // Put in all the new content
        foundDocument.putAll(updateContent);

        // Update and replace the document in the database
        foundCollection.replaceOne(filter, foundDocument);
        return JsonDocumentActionResult.SUCCESS;
    }

    /**
     * @see MongoRepository#deleteDocument(String, String, Bson)
     */
    public JsonDocumentActionResult deleteDocument(@NonNull String database, @NonNull String collection, @NonNull String documentId) {
        return this.deleteDocument(documentId, collection, Filters.eq("_id", documentId));
    }

    /**
     * Permanently delete a document.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to delete the document in.
     * @param filter Bson filter used to find the target document to delete.
     * @return Enum result of the return of the deleting.
     */
    public JsonDocumentActionResult deleteDocument(@NonNull String database, @NonNull String collection, @NonNull Bson filter) {
        // Get the collection from the database
        MongoCollection<Document> foundCollection = this.instance.getCollectionByIdentifier(database, collection);
        if (foundCollection == null) {
            return JsonDocumentActionResult.NO_COLLECTION;
        }

        // Check if the document exists
        Document foundDocument = foundCollection.find(filter).first();
        if (foundDocument == null) {
            return JsonDocumentActionResult.NO_EXIST;
        }

        // Delete the document
        foundCollection.deleteOne(filter);
        return JsonDocumentActionResult.SUCCESS;
    }

    /**
     * @see MongoRepository#getDocument(String, String, Bson)
     */
    public @Nullable Document getDocument(@NonNull String database, @NonNull String collection, @NonNull String documentId) {
        return this.getDocument(database, collection, Filters.eq(DOCUMENT_IDENTIFIER, documentId));
    }

    /**
     * Get a Mongo document from the target database and collection.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to get the document from.
     * @param filter Bson filter used to find the target document to query.
     * @return Instance of the full JSON document.
     */
    public @Nullable Document getDocument(@NonNull String database, @NonNull String collection, @NonNull Bson filter) {
        // Get the collection from the database
        MongoCollection<Document> foundCollection = this.instance.getCollectionByIdentifier(database, collection);
        if (foundCollection == null) {
            return null;
        }

        // Return the document based on the filter
        return foundCollection.find(filter).first();
    }

    /**
     * Get all existing documents within the target database and collection.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to get all documents from.
     * @return List containing instance of each full JSON document.
     */
    public @Nullable List<Document> getDocumentList(@NonNull String database, @NonNull String collection) {
        // Get the collection from the database
        MongoCollection<Document> foundCollection = this.instance.getCollectionByIdentifier(database, collection);
        if (foundCollection == null) {
            return null;
        }

        // Turn into list
        return foundCollection.find().into(new ArrayList<>());
    }

    /**
     * @return The Mongo instance (also known as client) type of this Mongo repository.
     */
    public @NonNull MongoInstanceType getType() {
        return this.type;
    }
}