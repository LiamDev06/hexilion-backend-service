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
 * Controller for handling localization based document related requests.
 */
@RestController
@RequestMapping("/localization")
public class LocalizationStoreController extends AbstractBaseMongoController {

    private static final @NonNull String LOCALIZATION_DATABASE = "localization";

    @Autowired
    public LocalizationStoreController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.MASTER, mongoDataManager);
    }

    @GetMapping("/get/{collection}")
    public AbstractBaseJsonResponse<Document> getLocalizationStore(@NonNull @PathVariable(value = "collection") String collection, @RequestParam("identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.get(LOCALIZATION_DATABASE, collection, Filters.eq("_id", identifier), response);
    }

    @GetMapping("/get/all/{collection}")
    public AbstractBaseJsonResponse<List<Document>> getLocalizationsFromCollection(@NonNull @PathVariable(value = "collection") String collection, @NonNull HttpServletResponse response) {
        return this.getAll(LOCALIZATION_DATABASE, collection, response);
    }
}