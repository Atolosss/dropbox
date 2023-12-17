package com.dropbox.support;

import com.dropbox.initializer.MongoDbInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = MongoDbInitializer.class)
@ActiveProfiles("test")
public abstract class MongoDatabaseAwareTestBase {
    @Autowired
    protected MongoTemplate mongoTemplate;

    protected abstract Map<String, String> getCollectionsName();

    protected abstract Set<String> getCollections();

    @BeforeEach
    void check() {
        getCollectionsName().keySet().stream().map(this::countRecordsInCollection).forEach(count -> assertThat(count).isZero());
    }

    @AfterEach
    void dropCollections() {
        getCollections().forEach(collection -> mongoTemplate.dropCollection(collection));
    }

    protected long countRecordsInCollection(final String collectionName) {
        return mongoTemplate.getCollection(collectionName).countDocuments();
    }
}
