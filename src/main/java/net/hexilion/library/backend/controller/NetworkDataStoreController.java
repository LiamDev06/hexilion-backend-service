package net.hexilion.library.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.model.Filters;
import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.controller.base.AbstractBaseMongoController;
import net.hexilion.library.backend.database.mongo.MongoDataManager;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling stored database network data related requests.
 */
@RestController
@RequestMapping("/datastore/network")
public class NetworkDataStoreController extends AbstractBaseMongoController {

    @Autowired
    public NetworkDataStoreController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.NETWORK, mongoDataManager);
    }

    @PostMapping("/create")
    public AbstractBaseJsonResponse<Document> createNetworkStoreObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.create(database, collection, body, identifier, response);
    }

    @PutMapping("/update")
    public AbstractBaseJsonResponse<Document> updateNetworkStoreObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.update(database, collection, body, Filters.eq("_id", identifier), response);
    }

    @DeleteMapping("/delete")
    public AbstractBaseJsonResponse<Document> deleteNetworkStoreObject(@NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.delete(database, collection, Filters.eq("_id", identifier), response);
    }

    @GetMapping("/get")
    public AbstractBaseJsonResponse<Document> getNetworkStoreObjectByIdentifier(@NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.get(database, collection, Filters.eq("_id", identifier), response);
    }
}