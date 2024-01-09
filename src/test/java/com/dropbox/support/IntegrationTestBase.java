package com.dropbox.support;

import com.dropbox.model.entity.User;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import com.dropbox.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

public class IntegrationTestBase extends DatabaseAwareTestBase {
    @LocalServerPort
    protected int localPort;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;
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
        return Set.of("users");
    }

    protected void createUser(final User user) {
        userRepository.save(user);
    }

}

