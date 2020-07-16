package com.sgannu.blog.repo;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import static java.util.Objects.nonNull;
/*
@Profile("dev")
@Configuration
@EnableReactiveMongoRepositories
@EnableConfigurationProperties(MongoProperties.class)
@Slf4j */
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {
    private final MongoProperties mongoProperties;
    private final ConfigurableEnvironment env;
    @Value("${spring.data.mongodb.port}")
    private String localPort;

    public MongoConfiguration(MongoProperties mongoProperties, ConfigurableEnvironment env) {
        this.mongoProperties = mongoProperties;
        this.env = env;
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getMongoClientDatabase();
    }

    @Override
    @Bean(destroyMethod = "close")
    public MongoClient reactiveMongoClient() {
        //Integer localPort = env.getProperty("spring.data.mongodb.port", Integer.class);
        // log.info("Starting mongodb on port: {}", localPort);

        if (nonNull(localPort)) {
            return MongoClients.create(String.format("mongodb://localhost:%s/%s", localPort, getDatabaseName()));
        }

        return MongoClients.create(mongoProperties.determineUri());
    }

    @Configuration
    @Import({EmbeddedMongoAutoConfiguration.class})
    public static class EmbeddedMongoConfiguration {
    }
}