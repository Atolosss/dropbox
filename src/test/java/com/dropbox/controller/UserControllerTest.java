package com.dropbox.controller;

//import com.dropbox.model.entity.User;
//import com.dropbox.model.openapi.FileRs;
//import com.dropbox.model.openapi.UserPatchRq;
//import com.dropbox.model.openapi.UserPatchRs;
//import com.dropbox.model.openapi.UserRegistrationRq;
//import com.dropbox.model.openapi.UserRegistrationRs;
//import com.dropbox.model.openapi.UserRs;
//import com.dropbox.support.DataProvider;
import com.dropbox.support.IntegrationTestBase;
//import org.junit.jupiter.api.Test;
//import org.springframework.core.ParameterizedTypeReference;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends IntegrationTestBase {

//    public static final String API = "api";
//    public static final String V_1 = "v1";
//    public static final String USERS = "users";
//    public static final String FILES = "files";
//
//    @Test
//    void postUserShouldWork() {
//        final UserRegistrationRq userRegistrationRq = DataProvider.prepareUserRegistrationRq().build();
//
//        final UserRegistrationRs userRegistrationRs = postUser(userRegistrationRq, 200);
//        assertThat(userRegistrationRs).isNotNull();
//        assertThat(userRepository.findUserByEmail(userRegistrationRq.getEmail())).isNotEmpty();
//    }
//
//    @Test
//    void getAllUsersShouldWork() {
//        final User user = DataProvider.prepareUser().build();
//        createUser(user);
//
//        assertThat(getAllUsers())
//            .usingRecursiveComparison()
//            .ignoringFields("lastName", "firstName", "dateOfBirth", FILES)
//            .isEqualTo(List.of(UserRs.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .build()));
//    }
//
//    @Test
//    void getUserFilesShouldWork() {
//        createUserAndFile();
//        final User user = userRepository.findAll().stream()
//            .findFirst().orElseThrow();
//        assertThat(getUserFiles(user.getId()))
//            .usingRecursiveComparison()
//            .ignoringFields("id", "fileType", "url", "userId")
//            .isEqualTo(List.of(FileRs.builder()
//                .name("How make doc")));
//    }
//
//    private List<FileRs> getUserFiles(final Long id) {
//        return webTestClient.get()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS, String.valueOf(id), FILES)
//                .build())
//            .exchange()
//            .expectStatus().isEqualTo(200)
//            .expectBody(new ParameterizedTypeReference<List<FileRs>>() {
//            })
//            .returnResult()
//            .getResponseBody();
//    }
//
//    private List<UserRs> getAllUsers() {
//        return webTestClient.get()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS)
//                .build())
//            .exchange()
//            .expectStatus().isEqualTo(200)
//            .expectBody(new ParameterizedTypeReference<List<UserRs>>() {
//            })
//            .returnResult()
//            .getResponseBody();
//    }
//
//    @Test
//    void patchUserShouldWork() {
//        final User user = DataProvider.prepareUser().build();
//        createUser(user);
//        final UserPatchRq userPatchRq = DataProvider.prepareUserPatchRq().build();
//
//        assertThat(patchUser(userPatchRq, user.getId()))
//            .isNotNull();
//        assertThat(userRepository.findById(user.getId()))
//            .isNotEmpty()
//            .get()
//            .usingRecursiveComparison()
//            .ignoringFields("createDateTime", "updateDateTime", "dateOfBirth")
//            .isEqualTo(User.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .files(List.of())
//                .lastName(userPatchRq.getLastName())
//                .firstName(userPatchRq.getFirstName())
//                .build());
//
//    }
//
//    @Test
//    void deleteUserShouldWork() {
//        final User user = DataProvider.prepareUser().build();
//        createUser(user);
//        deleteUser(user.getId());
//
//        assertThat(userRepository.findById(user.getId()))
//            .isEmpty();
//    }
//
//    @Test
//    void getUserShouldWork() {
//        final User user = DataProvider.prepareUser().build();
//        createUser(user);
//
//        assertThat(getUser(user.getId()))
//            .usingRecursiveComparison()
//            .ignoringFields("lastName", "firstName", "dateOfBirth", FILES)
//            .isEqualTo(UserRs.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .build());
//    }
//
//    private UserRs getUser(final Long id) {
//        return webTestClient.get()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS, String.valueOf(id))
//                .build())
//            .exchange()
//            .expectStatus().isEqualTo(200)
//            .expectBody(UserRs.class)
//            .returnResult()
//            .getResponseBody();
//    }
//
//    private void deleteUser(final Long id) {
//        webTestClient.delete()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS, String.valueOf(id))
//                .build())
//            .exchange()
//            .expectStatus().isEqualTo(200);
//    }
//
//    private UserRegistrationRs postUser(final UserRegistrationRq request, final int status) {
//        return webTestClient.post()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS)
//                .build())
//            .bodyValue(request)
//            .exchange()
//            .expectStatus().isEqualTo(status)
//            .expectBody(UserRegistrationRs.class)
//            .returnResult()
//            .getResponseBody();
//    }
//
//    private UserPatchRs patchUser(final UserPatchRq request, final Long id) {
//        return webTestClient.patch()
//            .uri(uriBuilder -> uriBuilder
//                .pathSegment(API, V_1, USERS, String.valueOf(id))
//                .build())
//            .bodyValue(request)
//            .exchange()
//            .expectBody(UserPatchRs.class)
//            .returnResult()
//            .getResponseBody();
//    }
}
