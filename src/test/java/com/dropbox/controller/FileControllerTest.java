package com.dropbox.controller;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.support.DataProvider;
import com.dropbox.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.gmm.demo.model.api.FilePatchRq;
import ru.gmm.demo.model.api.FileRs;
import ru.gmm.demo.model.api.FileUploadRq;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class FileControllerTest extends IntegrationTestBase {

    @Test
    void filePostShouldWork() {
        final User user = DataProvider.prepareUser().build();
        createUser(user);
        final FileUploadRq fileUploadRq = DataProvider.prepareFileUploadRq(userRepository.findUserByEmail("email").get().getId()).build();

        assertThat(postFile(fileUploadRq)).isNotNull();
        assertThat(fileRepository.findAll().stream().findFirst().get())
            .usingRecursiveComparison()
            .ignoringFields("id", "fileType", "url", "userId")
            .isEqualTo(File.builder()
                .name(fileUploadRq.getName()));
    }

    private FileRs postFile(final FileUploadRq request) {
        return webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "files")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(FileRs.class)
            .returnResult()
            .getResponseBody();
    }

    @Test
    void getFileShouldWork() {
        createUserAndFile();
        final File file = fileRepository.findAll().stream().findFirst().get();

        assertThat(getFileByUserId(file.getId()))
            .usingRecursiveComparison()
            .ignoringFields("id", "fileType", "url", "userId")
            .isEqualTo(FileRs.builder()
                .name(file.getName()));
    }

    private FileRs getFileByUserId(final String id) {
        return webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "files", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(200)
            .expectBody(FileRs.class)
            .returnResult()
            .getResponseBody();
    }

    @Test
    void deleteFileShouldWork() {
        createUserAndFile();

        final File file = fileRepository.findAll().stream().findFirst().get();
        deleteFileById(file.getId());

        assertThat(fileRepository.findById(file.getId())).isEmpty();
    }

    private void deleteFileById(final String id) {
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "files", id)
                .build())
            .exchange()
            .expectStatus().isEqualTo(200);
    }

    @Test
    void patchFileShouldWork() {
        createUserAndFile();

        final File file = fileRepository.findAll().stream().findFirst().get();
        final FilePatchRq filePatchRq = DataProvider.prepareFilePatchRq().build();

        assertThat(patchFile(file.getId(), filePatchRq))
            .usingRecursiveComparison()
            .ignoringFields("id", "fileType", "userId")
            .isEqualTo(FileRs.builder()
                .name(file.getName())
                .url(filePatchRq.getUrl()));
    }

    private FileRs patchFile(final String id, final FilePatchRq request) {
        return webTestClient.patch()
            .uri(uriBuilder -> uriBuilder
                .pathSegment("api", "v1", "files", id)
                .build())
            .bodyValue(request)
            .exchange()
            .expectBody(FileRs.class)
            .returnResult()
            .getResponseBody();
    }
}
