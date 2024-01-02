package com.dropbox.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SntBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(SntBot.class);
    private static final String BEGIN = "/start";
    private static final String SOMETHING_HAPPENED = "/something_happened";
    private static final String DROP = "/drop";

    public SntBot(@Value("${telegram.bot.token}") final String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        final var message = update.getMessage().getText();
        final var chatId = update.getMessage().getChatId();

        switch (message) {
            case BEGIN -> {
                final String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case SOMETHING_HAPPENED -> {
                final String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            default -> {
                final String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
        }
    }

    private void startCommand(final Long chatId, final String userName) {
        final var text = """
            Добро пожаловать в бот, %s!

            Здесь Вы сможете сообщить обо всех неисправностях или пожеланиях приложив фотограции.

            Для этого воспользуйтесь командами:
            /usd - курс доллара
            /eur - курс евро

            Дополнительные команды:
            /help - получение справки
            """;
        final var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    @Override
    public String getBotUsername() {
        return "komarov_cnt_bot";
    }

    private void sendMessage(final Long chatId, final String text) {
        final var chatIdStr = String.valueOf(chatId);
        final var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
