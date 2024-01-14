package com.dropbox.bot.handlers;

import com.dropbox.bot.SntBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.dropbox.bot.support.Keyboard.hermitageInlineKeyboardAb;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotCommandHandler {
    private final SntBot sntBot;
    private final UserRegistrationHandler userRegistrationHandler;

    public void handleMessage(final Message message) {
        final Long chatId = message.getChatId();
        final String userName = message.getChat().getUserName();
        final String text = message.getText();

        switch (text.toLowerCase()) {
            case "/start":
                startCommand(chatId, userName);
                userRegistrationHandler.registrationUser(message);
                try {
                    sntBot.execute(hermitageInlineKeyboardAb(chatId));
                } catch (TelegramApiException e) {
                    log.error("Ошибка при выполнении команды /start", e);
                    sendMessage(chatId, "Что-то пошло не так");
                }
                break;
            case "/да":
                try {
                    sntBot.execute(hermitageInlineKeyboardAb(chatId));
                } catch (TelegramApiException e) {
                    log.error("Ошибка при выполнении команды /да", e);
                    sendMessage(chatId, "Что-то пошло не так");
                }
                break;
            case "/нет":
                sendMessage(chatId, "Если вам что-то еще понадобится, напишите /start");
                break;
            default:
                sendMessage(chatId, "Что-то пошло не так");
        }
    }

    public void startCommand(final Long chatId, final String userName) {
        final var text = """
                Добро пожаловать в бот СНТ Аэрофлот-1, %s!

                Здесь Вы сможете сообщить обо всех неисправностях или пожеланиях приложив фотограции.

                """;
        final var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    public void sendMessage(final Long chatId, final String text) {
        final var chatIdStr = String.valueOf(chatId);
        final var sendMessage = new SendMessage(chatIdStr, text);
        try {
            sntBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }
}
