package com.dropbox.bot.handlers;

import com.dropbox.bot.enums.Buttons;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class CallbackQueryHandler {
    private final UserRepository userRepository;
    private final BotCommandHandler botCommandHandler;
    private final FileService fileService;
    private final FileHandler fileHandler;

    public void callbackQuery(final CallbackQuery callbackQuery) {
        final String callData = callbackQuery.getData();
        final long chatId = callbackQuery.getMessage().getChatId();
        final Long userId = callbackQuery.getFrom().getId();

        final User user = userRepository.findUserByTelegramUserId(userId);

        if (Buttons.BUTTON_01.getCallback().equals(callData)) {
            botCommandHandler.sendMessage(chatId, "Отправьте фотографию и опишите проблему");
        } else if (Buttons.BUTTON_02.getCallback().equals(callData)) {
            botCommandHandler.sendMessage(chatId, "Направляю вам список обращений: ");
            List<UserFile> files = user.getFiles();
            if (!files.isEmpty()) {
                int number = 0;
                for (UserFile f : files) {
                    number++;
                    botCommandHandler.sendMessage(chatId, number + ". " + f.getDescription());
                }
                botCommandHandler.sendMessage(chatId, "Вы что-то еще хотели сделать? Напишите в чат да или нет");
            } else {
                botCommandHandler.sendMessage(chatId, "От вас обращений пока не поступало");
            }
        } else if (Buttons.BUTTON_03.getCallback().equals(callData)) {
            botCommandHandler.sendMessage(chatId, "Направляю вам фотографии обращений: ");
            List<UserFile> keys = user.getFiles();
            if (!keys.isEmpty()) {
                for (UserFile k : keys) {
                    fileHandler.sendFiles(chatId, fileService.getFile(k.getKey()).getBase64());
                }
                botCommandHandler.sendMessage(chatId, "Вы что-то еще хотели сделать? Напишите в чат да или нет");
            } else {
                botCommandHandler.sendMessage(chatId, "От вас обращений пока не поступало");
            }
        }
    }
}
