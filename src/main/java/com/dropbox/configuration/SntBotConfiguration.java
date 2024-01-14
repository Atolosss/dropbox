package com.dropbox.configuration;

import com.dropbox.bot.SntBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class SntBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(final SntBot sntBot) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(sntBot);
            return api;
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
