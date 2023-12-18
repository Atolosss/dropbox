package com.dropbox.support;

import com.dropbox.initializer.MongoDbInitializer;
import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = MongoDbInitializer.class)
@ActiveProfiles("test")
public class IntegrationTestBase {
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
        user.setFiles(List.of(file));
        createUser(user);
        createFile(file);

    }
}
