package com.dropbox.support;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final DockerImageName IMAGE = DockerImageName.parse("mongo:latest");

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
        new MongoDBContainer(IMAGE)
            .withExposedPorts(27017);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MONGO_DB_CONTAINER
            .start();

        TestPropertyValues.of(
            "spring.data.mongodb.host=" + MONGO_DB_CONTAINER.getHost(),
            "spring.data.mongodb.port=" + MONGO_DB_CONTAINER.getFirstMappedPort()
        ).applyTo(applicationContext.getEnvironment());
    }
}
