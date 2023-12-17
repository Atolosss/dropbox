package com.dropbox.controller;

import com.dropbox.model.entity.User;
import com.dropbox.support.DataProvider;
import com.dropbox.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.gmm.demo.model.api.FileRs;
import ru.gmm.demo.model.api.UserPatchRq;
import ru.gmm.demo.model.api.UserPatchRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class UserControllerTest extends IntegrationTestBase {

    @Test
    void postUserShouldWork() {
        UserRegistrationRq userRegistrationRq = DataProvider.prepareUserRegistrationRq().build();

        UserRegistrationRs userRegistrationRs = postUser(userRegistrationRq, 200);
        assertThat(userRegistrationRs).isNotNull();
        assertThat(userRepository.findUserByEmail(userRegistrationRq.getEmail())).isNotEmpty();
    }

    @Test
    void getAllUsersShouldWork() {
        final User user = DataProvider.prepareUser().build();
        createUser(user);

        assertThat(getAllUsers())
            .usingRecursiveComparison()
            .ignoringFields("lastName", "firstName", "dateOfBirth", "files")
            .isEqualTo(List.of(UserRs.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build()));
    }

    private List<FileRs> getUserFiles(final String id) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users", id, "files")
                .build())
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(new ParameterizedTypeReference<List<FileRs>>() {
            })
            .returnResult()
            .getResponseBody();
    }

    private List<UserRs> getAllUsers() {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users")
                .build())
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(new ParameterizedTypeReference<List<UserRs>>() {
            })
            .returnResult()
            .getResponseBody();
    }

    @Test
    void patchUserShouldWork() {
        final User user = DataProvider.prepareUser().build();
        createUser(user);
        final UserPatchRq userPatchRq = DataProvider.prepareUserPatchRq().build();

        assertThat(patchUser(userPatchRq, user.getId()))
            .isNotNull();
        assertThat(userRepository.findById(user.getId()))
            .isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("createDateTime", "updateDateTime", "dateOfBirth")
            .isEqualTo(User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .files(List.of())
                .lastName(userPatchRq.getLastName())
                .firstName(userPatchRq.getFirstName())
                .build());

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
            .ignoringFields("lastName", "firstName", "dateOfBirth", "files")
            .isEqualTo(UserRs.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build());
    }

    private UserRs getUser(final String id) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(UserRs.class)
            .returnResult()
            .getResponseBody();
    }

    private void deleteUser(final String id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(200);
    }

    private UserRegistrationRs postUser(final UserRegistrationRq request, final int status) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody(UserRegistrationRs.class)
            .returnResult()
            .getResponseBody();
    }

    private UserPatchRs patchUser(final UserPatchRq request, final String id) {
        return webTestClient.patch()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "users", id)
                .build())
            .bodyValue(request)
            .exchange()
            .expectBody(UserPatchRs.class)
            .returnResult()
            .getResponseBody();
    }
}
