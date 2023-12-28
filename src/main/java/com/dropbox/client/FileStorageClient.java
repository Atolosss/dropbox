package com.dropbox.client;

import com.dropbox.model.dto.UploadFileDtoRq;
import com.dropbox.model.dto.UploadFileRs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

}
