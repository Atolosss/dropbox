package com.dropbox.bot;

import com.dropbox.bot.enums.Buttons;
import com.dropbox.controller.UserController;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import static com.dropbox.bot.support.Keyboard.hermitageInlineKeyboardAb;

@Component
@Slf4j
public class SntBot extends TelegramLongPollingBot {
    private final UserController userController;
    private final FileService fileService;
    private final UserRepository userRepository;

    public SntBot(@Value("${telegram.bot.token}") final String botToken, final UserController userController, final FileService fileService, final UserRepository userRepository) {
        super(botToken);
        this.userController = userController;
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    public static void saveByteArrayToFile(final byte[] byteArray, final String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFile(final Message message) throws TelegramApiException, IOException {
        final String fileId = message.getPhoto().get(0).getFileId();
        final String filePath = getFilePath(message.getPhoto().get(0).getFileId());
        final byte[] fileContent = downloadFileContent(filePath);
        final String encodedFile = encodeFileToBase64(fileContent);

        final UploadFileDtoRq uploadFileDtoRq = UploadFileDtoRq.builder()
            .name(fileId)
            .userId(2L)
            .base64Data(encodedFile)
            .build();

        final FileRs fileRs = fileService.createFile(uploadFileDtoRq);
        log.info(fileRs.toString());
    }

    private void sendFiles(final Long chatId, final String k) throws TelegramApiException {
        final byte[] fileContent = getDecode(k);
        final String filePath = "C:/Users/user/Desktop/foto.jpg";
        saveByteArrayToFile(fileContent, filePath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile().setMedia(filePath));

        execute(sendPhoto);
    }

    private byte[] getDecode(final String k) {
        return Base64.getDecoder().decode(fileService.getFile(k).getBase64());
    }

    private String getFilePath(final String fileId) throws TelegramApiException {
        final GetFile getFile = new GetFile(fileId);
        return execute(getFile).getFilePath();
    }

    private byte[] downloadFileContent(final String filePath) throws IOException, TelegramApiException {
        final File downloadFile = downloadFile(filePath);
        return Files.readAllBytes(downloadFile.toPath());
    }

    private String encodeFileToBase64(final byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public void handleMessage(final Message message) {
        final Long chatId = message.getChatId();
        final String userName = message.getChat().getUserName();
        final String text = message.getText();

        switch (text) {
            case "/start":
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
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }

    public void registrationUser(final Message msg) {
        if (userRepository.findUserByTelegramUserId(msg.getChatId()) == null) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            userController.createUser(UserRegistrationRq.builder()
                .telegramUserId(chatId)
                .firstName(chat.getFirstName())
                .lastName(chat.getLastName())
                .build());
        }
    }

    @Override
    public String getBotUsername() {
        return "komarov_cnt_bot";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(final Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            getFile(update.getMessage());
        }
    }

    public void callbackQueryHandler(final CallbackQuery callbackQuery) throws TelegramApiException {
        final String callData = callbackQuery.getData();
        final long chatId = callbackQuery.getMessage().getChatId();

        if (Buttons.BUTTON_01.getCallback().equals(callData)) {
            sendMessage(chatId, "Отправьте фотографию и опишите проблему");
        } else if (Buttons.BUTTON_02.getCallback().equals(callData)) {
            sendMessage(chatId, "Направляю вам список обращений: ");
        }
    }
}










