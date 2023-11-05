package net.hexilion.library.backend.controller.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.database.mongo.MongoDataManager;
import net.hexilion.library.backend.database.mongo.MongoRepository;
import net.hexilion.library.backend.database.mongo.enums.JsonDocumentActionResult;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import net.hexilion.library.backend.response.DeleteRequestJsonResponse;
import net.hexilion.library.backend.response.GetRequestJsonResponse;
import net.hexilion.library.backend.response.PostRequestJsonResponse;
import net.hexilion.library.backend.response.PutRequestJsonResponse;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import net.hexilion.library.backend.response.base.FetchedFrom;
import net.hexilion.library.backend.response.error.DataNotFoundResponse;
import net.hexilion.library.backend.response.error.InvalidJsonDocumentActionResponse;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Base controller to provide common functionality used in all controllers.
 */
public abstract class AbstractBaseMongoController extends MongoRepository {

    private final @NonNull ObjectMapper objectMapper;

    public AbstractBaseMongoController(@NonNull MongoInstanceType mongoInstanceType, @NonNull MongoDataManager mongoDataManager) {
        super(mongoInstanceType, mongoDataManager);

        this.objectMapper = new ObjectMapper();
    }

    /**
     * Create a new JSON document in the target database and collection in the
     * selected instance. Handles all the steps to create a document and handles
     * the exceptions that can occur during the creation.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to create the document in.
     * @param body JSON body contents included in the HTTP request. This content will make up the initial document upon creation.
     * @param identifier The identifier of the document. This will automatically be put as the '_id' value of the document.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    public AbstractBaseJsonResponse<Document> create(@NonNull String database, @NonNull String collection, @NonNull JsonNode body, @NonNull String identifier, @NonNull HttpServletResponse response) {
        // Create a Mongo Bson document and insert the request body contents
        Document document = new Document("_id", identifier);
        document.putAll(Document.parse(body.toString()));

        // Handle different identifier scenarios
        if (identifier.equals("")) {
            return new InvalidJsonDocumentActionResponse<>("The identifier is empty.", response);
        } else if (!document.containsKey("_id")) {
            return new InvalidJsonDocumentActionResponse<>("No '_id' field could be found in the JSON document.", response);
        } else if (!document.getString("_id").equals(identifier)) {
            return new InvalidJsonDocumentActionResponse<>("The '_id' field in the JSON document is not equal to the provided 'identifier' request parameter.", response);
        }

        // Try to create the JSON document
        JsonDocumentActionResult result = this.createDocument(database, collection, identifier, document);

        // Validate the result
        if (result == JsonDocumentActionResult.NO_COLLECTION) {
            return this.collectionDoesNotExistResponse(collection, response);
        } else if (result == JsonDocumentActionResult.ALREADY_EXISTS) {
            return this.documentWithIdentifierAlreadyExistResponse(identifier, response);
        }

        // The creation result was successful, return successful POST response
        return new PostRequestJsonResponse<>(document, response);
    }

    /**
     * Update an existing JSON document in the target database and collection in the
     * selected instance. Handles all the steps to update a document and handles
     * the exceptions that can occur during the updating.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to update the document in.
     * @param body JSON body contents included in the HTTP request. This content includes the updates to perform to the document. Can include new fields to append but also existing ones to replace.
     * @param filter Bson filter to find the target document to update.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    public AbstractBaseJsonResponse<Document> update(@NonNull String database, @NonNull String collection, @NonNull JsonNode body, @NonNull Bson filter, @NonNull HttpServletResponse response) {
        // Convert the request body contents to a map
        Map<String, Object> updateData = this.objectMapper.convertValue(body, Map.class);

        // The document identifier cannot be updated
        if (updateData.containsKey("_id")) {
            return new InvalidJsonDocumentActionResponse<>("You cannot modify the identifier ('_id') value.", response);
        }

        // Try to update the JSON document
        JsonDocumentActionResult result = this.updateDocument(database, collection, filter, updateData);

        // Validate the result
        if (result == JsonDocumentActionResult.NO_COLLECTION) {
            return this.collectionDoesNotExistResponse(collection, response);
        } else if (result == JsonDocumentActionResult.NO_EXIST) {
            return this.documentWithIdentifierDoesNotExistResponse(filter, response);
        }

        // Update was a success, get the document that was just updated
        Document document = this.getDocument(database, collection, filter);
        assert document != null;

        // Return successful PUT response
        return new PutRequestJsonResponse<>(document, response);
    }

    /**
     * Delete an existing JSON document in the target database and collection in the
     * selected instance. Handles all the steps to delete a document and handles
     * the exceptions that can occur during the deleting.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to delete the document in.
     * @param filter Bson filter to find the target document to delete.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    public AbstractBaseJsonResponse<Document> delete(@NonNull String database, @NonNull String collection, @NonNull Bson filter, @NonNull HttpServletResponse response) {
        // Get the document to be deleted
        Document documentToDelete = this.getDocument(database, collection, filter);

        // Try to delete the document
        JsonDocumentActionResult result = this.deleteDocument(database, collection, filter);

        // Validate the result
        if (result == JsonDocumentActionResult.NO_COLLECTION) {
            return this.collectionDoesNotExistResponse(collection, response);
        } else if (result == JsonDocumentActionResult.NO_EXIST) {
            return this.documentWithIdentifierDoesNotExistResponse(filter, response);
        }

        // The deletion result was successful, return successful DELETE response
        assert documentToDelete != null;
        return new DeleteRequestJsonResponse<>(documentToDelete, response);
    }

    /**
     * Retrieve an existing JSON document in the target database and collection in the
     * selected instance. Handles the exception of the document not existing and being null.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to get the document in.
     * @param filter Bson filter to find the target document to retrieve.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    public AbstractBaseJsonResponse<Document> get(@NonNull String database, @NonNull String collection, @NonNull Bson filter, @NonNull HttpServletResponse response) {
        // Get the JSON document
        Document document = this.getDocument(database, collection, filter);

        // The document does not exist
        if (document == null) {
            String identifier = filter.toBsonDocument().getFirstKey();
            return new DataNotFoundResponse<>(collection, identifier, response);
        }

        // The request was successful, return successful GET response
        FetchedFrom fetchedFrom = new FetchedFrom(database, collection);
        return new GetRequestJsonResponse<>(document, fetchedFrom, response);
    }

    /**
     * Retrieve all existing JSON documents in the target database and collection in the
     * selected instance. Handles the exception of the collection not existing and being null.
     *
     * @param database Name/identifier of the database to get the collection from.
     * @param collection Name/identifier of the collection to get all documents from.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    public AbstractBaseJsonResponse<List<Document>> getAll(@NonNull String database, @NonNull String collection, @NonNull HttpServletResponse response) {
        // Get a list of all the JSON documents
        List<Document> list = this.getDocumentList(database, collection);

        // The list does not exist
        if (list == null) {
            return new DataNotFoundResponse<>(collection, response);
        }

        // The request was successful, return successful GET response
        FetchedFrom fetchedFrom = new FetchedFrom(database, collection);
        return new GetRequestJsonResponse<>(list, fetchedFrom, response);
    }

    /**
     * Error response used for when a collection does not exist.
     *
     * @param collection Name/identifier of the collection, that does not exist.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    private InvalidJsonDocumentActionResponse<Document> collectionDoesNotExistResponse(@NonNull String collection, @NonNull HttpServletResponse response) {
        return new InvalidJsonDocumentActionResponse<>("The collection '" + collection + "' does not exist.", response);
    }

    /**
     * Error response used for when a document with the target
     * filter identification, does not exist.
     *
     * @param filter The filter used to find the document, that does not exist.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    private InvalidJsonDocumentActionResponse<Document> documentWithIdentifierDoesNotExistResponse(@NonNull Bson filter, @NonNull HttpServletResponse response) {
        return new InvalidJsonDocumentActionResponse<>("A document with identification '" + filter + "' does not exist.", response);
    }

    /**
     * Error response used for when a document with the target identifier already exists.
     *
     * @param identifier Identifier of the document that already exists.
     * @param response HTTP response from the request.
     * @return Custom JSON response object.
     */
    private InvalidJsonDocumentActionResponse<Document> documentWithIdentifierAlreadyExistResponse(@NonNull String identifier, @NonNull HttpServletResponse response) {
        return new InvalidJsonDocumentActionResponse<>("A document with identifier '" + identifier + "' already exist.", response);
    }
}