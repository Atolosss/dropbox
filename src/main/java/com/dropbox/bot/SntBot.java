package com.dropbox.bot;

import com.dropbox.bot.enums.Buttons;
import com.dropbox.configuration.TelegramBotConfiguration;
import com.dropbox.constant.ErrorCode;
import com.dropbox.constant.Messages;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.repository.UserRepository;
import com.dropbox.service.FileService;
import com.dropbox.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

import static com.dropbox.bot.support.Keyboard.prepareKeyBoardSendMessage;

@Component
@Slf4j
public class SntBot extends TelegramLongPollingBot {
    private final FileService fileService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TelegramBotConfiguration telegramBotConfiguration;

    public SntBot(final TelegramBotConfiguration telegramBotConfiguration,
                  final FileService fileService,
                  final UserService userService,
                  final UserRepository userRepository) {
        super(telegramBotConfiguration.getToken());

        this.fileService = fileService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.telegramBotConfiguration = telegramBotConfiguration;
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getName();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            downloadFileProcess(update.getMessage());
        }
    }

    private String getFilePath(final String fileId) throws TelegramApiException {
        final GetFile getFile = new GetFile(fileId);
        return execute(getFile).getFilePath();
    }

    private byte[] downloadFileContent(final String filePath) throws IOException, TelegramApiException {
        final File downloadFile = downloadFile(filePath);
        return Files.readAllBytes(downloadFile.toPath());
    }

    private void downloadFileProcess(final Message message) {
        try {
            final String filePath = getFilePath(message.getPhoto().get(0).getFileId());
            final byte[] fileContent = downloadFileContent(filePath);
            sendMessage(message.getChatId(), fileService.uploadFile(message, fileContent));
        } catch (TelegramApiException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void startProcess(Message message) {
        sendText(message.getChatId(), message.getChat().getUserName());
        userService.registrationUser(message);
        try {
            execute(prepareKeyBoardSendMessage(message.getChatId()));
        } catch (TelegramApiException e) {
            log.error(ErrorCode.ERR_CODE_003.getDescription(), e);
            sendMessage(message.getChatId(), ErrorCode.ERR_CODE_003.getDescription());
        }
    }

    public void sendFiles(final Long chatId, final String k) {
        try {
            final byte[] fileContent = Base64.getDecoder().decode(k);

            if (fileContent != null) {
                try (InputStream is = new ByteArrayInputStream(fileContent)) {
                    execute(SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(is, "send.jpg"))
                        .build());
                } catch (IOException e) {
                    log.error("Ошибка при отправке изображения в Telegram.", e);
                    sendMessage(chatId, "Ошибка при отправке изображения в Telegram.");
                }
            } else {
                sendMessage(chatId, "Ошибка: файл не найден или его кодировка некорректна");
            }
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке изображения.", e);
            sendMessage(chatId, "Ошибка при отправке изображения.");
        }
    }

    public void callbackQuery(final CallbackQuery callbackQuery) {
        final String callData = callbackQuery.getData();
        final long chatId = callbackQuery.getMessage().getChatId();
        final Long userId = callbackQuery.getFrom().getId();

        final User user = userRepository.findUserByTelegramUserId(userId)
            .orElseThrow();

        if (Buttons.BUTTON_01.getCallback().equals(callData)) {
            sendMessage(chatId, "Отправьте фотографию и опишите проблему");
        } else if (Buttons.BUTTON_02.getCallback().equals(callData)) {
            sendMessage(chatId, "Направляю вам список обращений: ");
            List<UserFile> files = user.getFiles();
            if (!files.isEmpty()) {
                int number = 0;
                for (UserFile f : files) {
                    number++;
                    sendMessage(chatId, number + ". " + f.getDescription());
                }
                sendMessage(chatId, "Вы что-то еще хотели сделать? Напишите в чат да или нет");
            } else {
                sendMessage(chatId, "От вас обращений пока не поступало");
            }
        } else if (Buttons.BUTTON_03.getCallback().equals(callData)) {
            sendMessage(chatId, "Направляю вам фотографии обращений: ");
            List<UserFile> userFileList = user.getFiles();
            if (!userFileList.isEmpty()) {
                for (UserFile userFile : userFileList) {
                    String base64 = fileService.downloadFile(userFile.getKey()).getBase64();
                    sendFiles(chatId, base64);
                }
                sendMessage(chatId, "Вы что-то еще хотели сделать? Напишите в чат да или нет");
            } else {
                sendMessage(chatId, "От вас обращений пока не поступало");
            }
        }
    }

    public void handleMessage(final Message message) {
        final Long chatId = message.getChatId();
        final String text = message.getText();

        switch (text.toLowerCase()) {
            case "/start":
                startProcess(message);
                break;
            case "да":
                try {
                    execute(prepareKeyBoardSendMessage(chatId));
                } catch (TelegramApiException e) {
                    log.error("Ошибка при выполнении команды: да", e);
                    sendMessage(chatId, Messages.SOMETHING_WENT_WRONG);
                }
                break;
            case "нет":
                sendMessage(chatId, "Если вам что-то еще понадобится, напишите привет");
                break;
            case "привет":
                try {
                    execute(prepareKeyBoardSendMessage(chatId));
                } catch (TelegramApiException e) {
                    log.error("Ошибка при выполнении команды: привет", e);
                    sendMessage(chatId, Messages.SOMETHING_WENT_WRONG);
                }
                break;
            default:

                sendMessage(chatId, Messages.SOMETHING_WENT_WRONG);
        }
    }

    public void sendText(final Long chatId, final String userName) {
        final var text = """
            Добро пожаловать в бот СНТ Аэрофлот-1, %s!

            Здесь Вы сможете сообщить обо всех неисправностях или пожеланиях приложив фотограции.

            """;
        final var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    public void sendMessage(final Long chatId, final String text) {
        final var chatIdStr = String.valueOf(chatId);
        try {
            final var sendMessage = new SendMessage(chatIdStr, text);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

