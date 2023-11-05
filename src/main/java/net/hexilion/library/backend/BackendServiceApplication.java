package net.hexilion.library.backend;

import jakarta.annotation.PostConstruct;
import net.hexilion.library.backend.database.mongo.MongoDataManager;
import net.hexilion.library.backend.database.mongo.MongoInstance;
import net.hexilion.library.backend.database.mongo.enums.MongoInstanceType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Main class for the backend service application.
 * This class initializes the Spring Boot application and the Mongo data storage solution.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class BackendServiceApplication extends SpringBootServletInitializer {

    private static final @NonNull Logger log = Logger.getLogger("backend-service");
    private static final @NonNull String PRODUCTION_PROFILE = "production";

    @Autowired
    private MongoDataManager mongoDataManager;

    public BackendServiceApplication() { }

    /**
     * Main start method to start the backend service application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Start
        log.info("Starting backend service...");

        // Create the Spring application
        SpringApplication springApplication = new SpringApplication(BackendServiceApplication.class);
        springApplication.setAdditionalProfiles(PRODUCTION_PROFILE);

        // Check if a specific application.properties flag is set
        if (args.length > 0) {
            // Get the value of the flag
            String propertiesFile = args[0];

            // Set the location of the properties file
            Map<String, Object> properties = Collections.singletonMap("spring.config.name", propertiesFile);
            springApplication.setDefaultProperties(properties);
        }

        // Run and start the application
        springApplication.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BackendServiceApplication.class);
    }

    @PostConstruct
    private void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down backend service...");

            // Close the connections to all Mongo instances
            for (MongoInstanceType type : MongoInstanceType.VALUES) {
                MongoInstance instance = this.mongoDataManager.getInstanceByType(type);

                if (instance == null) {
                    log.severe("The " + type.name() + " Mongo instance could not be shutdown, it does not exist!");
                } else {
                    instance.shutdown();
                }
            }

            log.info("Backend service was shutdown.");
        }));
    }
}