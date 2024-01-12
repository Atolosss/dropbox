package com.dropbox.controller;

import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.model.openapi.UserRegistrationRs;
import com.dropbox.model.openapi.UserRs;
import com.dropbox.support.DataProvider;
import com.dropbox.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class UserControllerTest extends IntegrationTestBase {

    public static final String API = "api";
    public static final String V_1 = "v1";
    public static final String USERS = "users";

    @Test
    void postUserShouldWork() {
        final UserRegistrationRq userRegistrationRq = DataProvider.prepareUserRegistrationRq().build();

        assertThat(postUser(userRegistrationRq, 200)).isNotNull();
        assertThat(userRepository.findUserByTelegramUserId(userRegistrationRq.getTelegramUserId()))
            .usingRecursiveComparison()
            .ignoringFields("id", "createDateTime")
            .isEqualTo(User.builder()
                .telegramUserId(userRegistrationRq.getTelegramUserId())
                .firstName(userRegistrationRq.getFirstName())
                .lastName(userRegistrationRq.getLastName()).build());
    }

    @Test
    void deleteUserShouldWork() {
        final User user = DataProvider.prepareUser().build();
        createUser(user);
        deleteUser(user.getId());

        assertThat(userRepository.findById(user.getId()))
            .isEmpty();
    }

    @Test
    void getUserShouldWork() {
        final User user = DataProvider.prepareUser().build();
        createUser(user);

        assertThat(getUser(user.getId()))
            .usingRecursiveComparison()
            .ignoringFields("id", "telegramUserId")
            .isEqualTo(UserRs.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build());
    }

    private UserRs getUser(final Long id) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(API, V_1, USERS, String.valueOf(id))
                .build())
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(UserRs.class)
            .returnResult()
            .getResponseBody();
    }

    private void deleteUser(final Long id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(API, V_1, USERS, String.valueOf(id))
                .build())
            .exchange()
            .expectStatus().isEqualTo(200);
    }

    private UserRegistrationRs postUser(final UserRegistrationRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(API, V_1, USERS)
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserRegistrationRs.class)
            .returnResult()
            .getResponseBody();
    }

}
