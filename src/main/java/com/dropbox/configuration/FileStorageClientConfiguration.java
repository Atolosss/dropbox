package com.dropbox.configuration;

import com.dropbox.client.FileStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FileStorageClientConfiguration {

    @Bean
    public FileStorageClient fileStorageClient() {
        final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();
        return new FileStorageClient(webClient);
    }
}
