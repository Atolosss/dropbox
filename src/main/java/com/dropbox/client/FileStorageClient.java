package com.dropbox.client;

import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FileStorageClient {
    private final WebClient webClient;

    public UploadFileRs uploadFile(final UploadFileDtoRq uploadFileDtoRq) {
        return webClient.post()
            .uri(uriBuilder -> uriBuilder.pathSegment("api", "v1", "files")
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(uploadFileDtoRq))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(UploadFileRs.class)
            .block();
    }

    public DataBuffer downloadFile(final String id) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.pathSegment("api", "v1", "files", id)
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToFlux(clientResponse -> clientResponse.body(BodyExtractors.toDataBuffers()))
            .transform(DataBufferUtils::join)
            .as(Mono::from)
            .block();
    }

    public List<UploadFileDtoRs> getFileListByUserId(final Long userId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.pathSegment("api", "v1", "files")
                .queryParam(String.valueOf(userId))
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<UploadFileDtoRs>>() {
            })
            .block();
    }

}
