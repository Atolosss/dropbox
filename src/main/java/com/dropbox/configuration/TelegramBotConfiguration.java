package com.dropbox.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("telegram.bot")
public class TelegramBotConfiguration {
    private String name;
    private String token;

}
