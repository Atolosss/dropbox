package com.dropbox.support;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IntegrationTestBase extends MongoDatabaseAwareTestBase {
    @LocalServerPort
    protected int localPort;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected FileRepository fileRepository;

    @BeforeEach
    void beforeEach() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + localPort)
            .build();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
    }

    @Override
    protected Map<String, String> getCollectionsName() {
        Map<String, String> collections = new HashMap<>();
        collections.put("users", "test");
        collections.put("files", "test");
        return collections;
    }

    @Override
    protected Set<String> getCollections() {
        return Set.of("users", "files");
    }

    protected void createUser(final User user) {
        userRepository.save(user);
    }

    protected void createFile(final File file) {
        fileRepository.save(file);
    }

    protected void createUserAndFile() {
        final User user = DataProvider.prepareUser().build();
        final File file = DataProvider.prepareFile().build();
        file.setUserId(user.getId());
        createUser(user);
        createFile(file);
    }
}
