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
 * Controller for handling user related requests.
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractBaseMongoController {

    private static final @NonNull String USER_DATABASE = "user";
    private static final @NonNull String USER_COLLECTION = "users";

    @Autowired
    public UserController(@NonNull MongoDataManager mongoDataManager) {
        super(MongoInstanceType.NETWORK, mongoDataManager);
    }

    @PostMapping("/create")
    public AbstractBaseJsonResponse<Document> createUserObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "uuid") String userUuid, @NonNull HttpServletResponse response) {
        return this.create(USER_DATABASE, USER_COLLECTION, body, userUuid, response);
    }

    @PutMapping("/update")
    public AbstractBaseJsonResponse<Document> updateUserObject(@RequestBody @NonNull JsonNode body, @NonNull @RequestParam(value = "uuid") String userUuid, @NonNull HttpServletResponse response) {
        return this.update(USER_DATABASE, USER_COLLECTION, body, Filters.eq("_id", userUuid), response);
    }

    @GetMapping("/get")
    public AbstractBaseJsonResponse<Document> getInstanceByUuid(@NonNull @RequestParam(value = "uuid") String userUuid, @NonNull HttpServletResponse response) {
        return this.get(USER_DATABASE, USER_COLLECTION, Filters.eq("_id", userUuid), response);
    }
}