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
 * Controller for handling stored database game data related requests.
 */
@RestController
@RequestMapping("/datastore/game")
public class GameDataStoreController extends AbstractBaseMongoController {

    @Autowired
    public GameDataStoreController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.GAME, mongoDataManager);
    }

    @PostMapping("/create")
    public AbstractBaseJsonResponse<Document> createGameDataStoreObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.create(database, collection, body, identifier, response);
    }

    @PutMapping("/update")
    public AbstractBaseJsonResponse<Document> updateGameDataStoreObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.update(database, collection, body, Filters.eq("_id", identifier), response);
    }

    @DeleteMapping("/delete")
    public AbstractBaseJsonResponse<Document> deleteGameDataStoreObject(@NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.delete(database, collection, Filters.eq("_id", identifier), response);
    }

    @GetMapping("/get")
    public AbstractBaseJsonResponse<Document> getGameDataStoreObjectByIdentifier(@NonNull @RequestParam(value = "database") String database, @NonNull @RequestParam(value = "collection") String collection, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.get(database, collection, Filters.eq("_id", identifier), response);
    }
}