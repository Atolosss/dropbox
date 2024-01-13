package com.dropbox.bot;

import com.dropbox.bot.enums.Buttons;
import com.dropbox.controller.UserController;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

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

    private byte[] getDecode(final String k) {
        return Base64.getDecoder().decode(k);
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
            var userId = msg.getFrom().getId();
            var chat = msg.getChat();

            userController.createUser(UserRegistrationRq.builder()
                    .telegramUserId(userId)
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
        final Long userId = callbackQuery.getFrom().getId();

        User user = userRepository.findUserByTelegramUserId(userId);

        if (Buttons.BUTTON_01.getCallback().equals(callData)) {
            sendMessage(chatId, "Отправьте фотографию и опишите проблему");
        } else if (Buttons.BUTTON_02.getCallback().equals(callData)) {
            sendMessage(chatId, "Направляю вам список обращений: ");
            List<UserFile> files = user.getFiles();
            if (!files.isEmpty()) {
                for (UserFile f : files) {
                    sendMessage(chatId, f.getDescription());
                }
            } else {
                sendMessage(chatId, "От вас обращений пока не поступало");
            }
        } else if (Buttons.BUTTON_03.getCallback().equals(callData)) {
            sendMessage(chatId, "Направляю вам фотографии обращений: ");
            List<UserFile> keys = user.getFiles();
            if (!keys.isEmpty()) {
                for (UserFile k : keys) {
                    sendFiles(chatId, fileService.getFile(k.getKey()).getBase64());
                }
            } else {
                sendMessage(chatId, "От вас обращений пока не поступало");
            }
        }
    }

    private void getFile(final Message message) throws TelegramApiException, IOException {
        final String fileId = message.getPhoto().get(0).getFileId();
        final String fileCaptcha = message.getCaption();
        final String filePath = getFilePath(fileId);
        final byte[] fileContent = downloadFileContent(filePath);
        final String encodedFile = encodeFileToBase64(fileContent);

        final UploadFileDtoRq uploadFileDtoRq = UploadFileDtoRq.builder()
                .name(fileCaptcha)
                .userId(message.getFrom().getId())
                .base64Data(encodedFile)
                .build();

        final FileRs fileRs = fileService.createFile(uploadFileDtoRq);
        log.info(fileRs.toString());
    }

    private void sendFiles(final Long chatId, final String k) throws TelegramApiException {
        final byte[] fileContent = getDecode(k);

        if (fileContent != null) {
            try (InputStream is = new ByteArrayInputStream(fileContent)) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(new InputFile(is, "foto.jpg"));

                execute(sendPhoto);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при отправке изображения в Telegram.", e);
            }
        } else {
            sendMessage(chatId, "Ошибка: файл не найден или его кодировка некорректна");
        }
    }
}











