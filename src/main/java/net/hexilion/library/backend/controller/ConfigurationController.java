package net.hexilion.library.backend.controller;

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

import java.util.List;

/**
 * Controller for handling configuration related requests.
 */
@RestController
@RequestMapping("/configuration")
public class ConfigurationController extends AbstractBaseMongoController {

    private static final @NonNull String CONFIGURATION_DATABASE = "configuration";

    @Autowired
    public ConfigurationController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.MASTER, mongoDataManager);
    }

    @GetMapping("/get/{collection}")
    public AbstractBaseJsonResponse<Document> getConfiguration(@NonNull @PathVariable(value = "collection") String collection, @RequestParam("identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.get(CONFIGURATION_DATABASE, collection, Filters.eq("_id", identifier), response);
    }

    @GetMapping("/get/all/{collection}")
    public AbstractBaseJsonResponse<List<Document>> getConfigurationsFromCollection(@NonNull @PathVariable(value = "collection") String collection, @NonNull HttpServletResponse response) {
        return this.getAll(CONFIGURATION_DATABASE, collection, response);
    }
}