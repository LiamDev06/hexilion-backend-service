package net.hexilion.library.backend.database.mongo.enums;

/**
 * Represents results for actions regarding JSON database documents.
 */
public enum JsonDocumentActionResult {

    /**
     * The target collection does not exist.
     */
    NO_COLLECTION,

    /**
     * The JSON document does not exist within the target collection.
     */
    NO_EXIST,

    /**
     * The JSON document already exists within the target collection.
     */
    ALREADY_EXISTS,

    /**
     * The action for the JSON document was successful issues.
     */
    SUCCESS;

}