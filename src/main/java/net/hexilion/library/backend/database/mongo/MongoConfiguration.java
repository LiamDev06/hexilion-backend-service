package net.hexilion.library.backend.database.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.logging.Logger;

/**
 * Enables Bean access for the Mongo data manager.
 */
@Configuration
public class MongoConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public MongoDataManager mongoDataManager(Logger logger) {
        return new MongoDataManager(logger, this.environment);
    }

    @Bean
    public Logger logger() {
        return Logger.getLogger("backend-service");
    }
}