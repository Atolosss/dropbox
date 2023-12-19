package com.dropbox.support;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import com.dropbox.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Set;

public class IntegrationTestBase extends DatabaseAwareTestBase {
    @LocalServerPort
    protected int localPort;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected FileRepository fileRepository;
    @Autowired
    protected FileService fileService;
    @Autowired
    protected UserService userService;

    @BeforeEach
    void beforeEach() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + localPort)
            .build();
    }

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("file", "users");
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
        file.setUser(user);
        user.setFiles(List.of(file));
        createUser(user);
        createFile(file);

    }
}
