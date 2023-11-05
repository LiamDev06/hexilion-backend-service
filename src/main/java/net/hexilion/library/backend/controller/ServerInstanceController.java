package net.hexilion.library.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.model.Filters;
import jakarta.servlet.http.HttpServletResponse;
import net.hexilion.library.backend.controller.base.AbstractBaseMongoController;
import net.hexilion.library.backend.database.mongo.MongoDataManager;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import net.hexilion.library.backend.response.base.AbstractBaseJsonResponse;
import net.hexilion.library.backend.response.error.MissingParameterResponse;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling server related requests regarding Minecraft instances.
 */
@RestController
@RequestMapping("/server/instance")
public class ServerInstanceController extends AbstractBaseMongoController {

    private static final @NonNull String NETWORK_DATABASE = "network";
    private static final @NonNull String INSTANCE_COLLECTION = "instances";

    @Autowired
    public ServerInstanceController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.MASTER, mongoDataManager);
    }

    @PostMapping("/create")
    public AbstractBaseJsonResponse<Document> createInstanceObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.create(NETWORK_DATABASE, INSTANCE_COLLECTION, body, identifier, response);
    }

    @PutMapping("/update")
    public AbstractBaseJsonResponse<Document> updateInstanceObject(@RequestBody @NonNull JsonNode body, @Nullable @RequestParam(value = "identifier", required = false) String identifier, @RequestParam(value = "port", required = false) Integer port, @NonNull HttpServletResponse response) {
        if (identifier != null) {
            return this.update(NETWORK_DATABASE, INSTANCE_COLLECTION, body, Filters.eq("_id", identifier), response);
        } else if (port != null) {
            return this.update(NETWORK_DATABASE, INSTANCE_COLLECTION, body, Filters.eq("port", port), response);
        } else {
            return new MissingParameterResponse<>(response);
        }
    }

    @DeleteMapping("/delete")
    public AbstractBaseJsonResponse<Document> deleteInstanceObject(@NonNull @RequestParam(value = "identifier") String identifier, @NonNull HttpServletResponse response) {
        return this.delete(NETWORK_DATABASE, INSTANCE_COLLECTION, Filters.eq("_id", identifier), response);
    }

    @GetMapping("/get")
    public AbstractBaseJsonResponse<Document> getInstanceByIdentifier(@Nullable @RequestParam(value = "identifier", required = false) String identifier, @RequestParam(value = "port", required = false) @Nullable Integer port, @NonNull HttpServletResponse response) {
        if (identifier != null) {
            return this.get(NETWORK_DATABASE, INSTANCE_COLLECTION, Filters.eq("_id", identifier), response);
        } else if (port != null) {
            return this.get(NETWORK_DATABASE, INSTANCE_COLLECTION, Filters.eq("port", port), response);
        } else {
            return new MissingParameterResponse<>(response);
        }
    }

    @GetMapping("/get/all")
    public AbstractBaseJsonResponse<List<Document>> getStoredInstances(@NonNull HttpServletResponse response) {
        return this.getAll(NETWORK_DATABASE, INSTANCE_COLLECTION, response);
    }
}