package com.dropbox.configuration;

import com.dropbox.bot.SntBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class SntBotConfiguration {

    public final SntBot sntBot;

    public SntBotConfiguration(final SntBot sntBot) {
        this.sntBot = sntBot;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(final SntBot sntBot) throws TelegramApiException {
        final var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(sntBot);
        return api;
    }
}
