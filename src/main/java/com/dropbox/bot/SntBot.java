package com.dropbox.bot;

import com.dropbox.controller.FileController;
import com.dropbox.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SntBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(SntBot.class);
    private static final String BEGIN = "/start";
    private static final String UPLOAD = "/upload";
    private static final String DOWNLOAD = "/download";

    public SntBot(@Value("${telegram.bot.token}") final String botToken) {
        super(botToken);
    }

    public static SendMessage hermitageInlineKeyboardAb(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите дальнейшие действие");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Регистрация пользователя");
        inlineKeyboardButton1.setCallbackData("РЕГИСТРАЦИЯ");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Сообщить о проблеме");
        inlineKeyboardButton2.setCallbackData("ПРОБЛЕМА");

        rowInline1.add(inlineKeyboardButton1);
        rowInline1.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton21 = new InlineKeyboardButton();
        inlineKeyboardButton21.setText("Список обращений");
        inlineKeyboardButton21.setCallbackData("ОБРАЩЕНИЯ");

        InlineKeyboardButton inlineKeyboardButton22 = new InlineKeyboardButton();
        inlineKeyboardButton22.setText("Список фотографий");
        inlineKeyboardButton22.setCallbackData("ФОТО");

        rowInline2.add(inlineKeyboardButton21);
        rowInline2.add(inlineKeyboardButton22);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
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
                hermitageInlineKeyboardAb(chatId);
            }
            case UPLOAD -> {

            }
            default -> {
                final String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
        }
    }

    private void startCommand(final Long chatId, final String userName) {
        final var text = """
            Добро пожаловать в бот СНТ Аэрофлот-1, %s!

            Здесь Вы сможете сообщить обо всех неисправностях или пожеланиях приложив фотограции.

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
