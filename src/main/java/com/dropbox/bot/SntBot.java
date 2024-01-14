package com.dropbox.bot;

import com.dropbox.bot.handlers.BotCommandHandler;
import com.dropbox.bot.handlers.CallbackQueryHandler;
import com.dropbox.bot.handlers.FileHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class SntBot extends TelegramLongPollingBot {
    private final CallbackQueryHandler callbackQueryHandler;
    private final BotCommandHandler botCommandHandler;
    private final FileHandler fileHandler;

    public SntBot(@Value("${telegram.bot.token}") final String botToken, final CallbackQueryHandler callbackQueryHandler, final BotCommandHandler botCommandHandler, final FileHandler fileHandler) {
        super(botToken);
        this.callbackQueryHandler = callbackQueryHandler;
        this.botCommandHandler = botCommandHandler;
        this.fileHandler = fileHandler;
    }

    @Override
    public String getBotUsername() {
        return "komarov_cnt_bot";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            botCommandHandler.handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.callbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            fileHandler.getFile(update.getMessage());
        }
    }
}
