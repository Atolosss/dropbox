package com.dropbox.bot;

import com.dropbox.controller.UserController;
import com.dropbox.model.openapi.FileMetaRs;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class SntBot extends TelegramLongPollingBot {
//    refactor service bot

    private static final String BEGIN = "/start";
    @Autowired
    private final UserController userController;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FileService fileService;

    public SntBot(@Value("${telegram.bot.token}") final String botToken, final UserController userController, final UserRepository userRepository, final FileService fileService) {
        super(botToken);
        this.userController = userController;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    public static SendMessage hermitageInlineKeyboardAb(final long chatId) {
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите дальнейшие действие:");

        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        final List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Сообщить о проблеме");
        inlineKeyboardButton1.setCallbackData("ПРОБЛЕМА");

        rowInline1.add(inlineKeyboardButton1);

        final List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        final InlineKeyboardButton inlineKeyboardButton21 = new InlineKeyboardButton();
        inlineKeyboardButton21.setText("Список обращений");
        inlineKeyboardButton21.setCallbackData("ОБРАЩЕНИЯ");

        final InlineKeyboardButton inlineKeyboardButton22 = new InlineKeyboardButton();
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

    @SneakyThrows
    @Override
    public void onUpdateReceived(final Update update) {
        String fileId = update.getMessage().getPhoto().get(0).getFileId();
        GetFile getFile = new GetFile(fileId);
        String filePath = execute(getFile).getFilePath();
        File downloadFile = downloadFile(filePath);
        byte[] fileContent = Files.readAllBytes(downloadFile.toPath());
        String encoded = Base64.getEncoder().encodeToString(fileContent);
        UploadFileDtoRq uploadFileDtoRq = UploadFileDtoRq.builder()
            .name(getFile.getFileId())
            .userId(2L)
            .base64Data(encoded)
            .build();
        FileRs fileRs = fileService.createFile(uploadFileDtoRq);
        log.info(fileRs.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleMessage(final Message message) {
        Long chatId = message.getChatId();
        String userName = message.getChat().getUserName();
        String text = message.getText();

        switch (text) {
            case BEGIN:
                startCommand(chatId, userName);
                registrationUser(message);
                try {
                    execute(hermitageInlineKeyboardAb(chatId));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                sendMessage(chatId, "Что то пошло не так");
                break;
        }
    }

    private void handleCallbackQuery(final CallbackQuery callbackQuery) {
        String callData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        if ("ПРОБЛЕМА".equals(callData)) {
            sendMessage(chatId, "парам па па");
        } else if ("ОБРАЩЕНИЯ".equals(callData)) {
            List<FileMetaRs> files = fileService.getListMetaFiles(chatId);
            for (FileMetaRs f : files) {
                sendMessage(chatId, f.getName());
            }
        }
    }

    private void registrationUser(final Message msg) {
        if (userRepository.findUserByTelegramUserId(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            userController.createUser(UserRegistrationRq.builder().telegramUserId(chatId).firstName(chat.getFirstName()).lastName(chat.getLastName()).build());
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
            log.error("Ошибка отправки сообщения", e);
        }
    }

}
