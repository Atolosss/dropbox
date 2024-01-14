package com.dropbox.bot.handlers;

import com.dropbox.bot.SntBot;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileHandler {
    private final SntBot sntBot;
    private final FileService fileService;
    private final BotCommandHandler botCommandHandler;

    private byte[] getDecode(final String k) {
        return Base64.getDecoder().decode(k);
    }

    private String getFilePath(final String fileId) throws TelegramApiException {
        final GetFile getFile = new GetFile(fileId);
        return sntBot.execute(getFile).getFilePath();
    }

    private byte[] downloadFileContent(final String filePath) throws IOException, TelegramApiException {
        final File downloadFile = sntBot.downloadFile(filePath);
        return Files.readAllBytes(downloadFile.toPath());
    }

    private String encodeFileToBase64(final byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public void getFile(final Message message) throws TelegramApiException, IOException {
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
        if (fileCaptcha != null) {
            final FileRs fileRs = fileService.createFile(uploadFileDtoRq);
            log.info(fileRs.toString());
            botCommandHandler.sendMessage(message.getChatId(), "Ваше обращение принято, могу ли вам еще чем нибудь помочь?");
        }
        botCommandHandler.sendMessage(message.getChatId(), "Требуется описать проблему!");
    }

    public void sendFiles(final Long chatId, final String k) {
        try {
            final byte[] fileContent = getDecode(k);

            if (fileContent != null) {
                try (InputStream is = new ByteArrayInputStream(fileContent)) {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setPhoto(new InputFile(is, "send.jpg"));

                    sntBot.execute(sendPhoto);
                } catch (IOException e) {
                    log.error("Ошибка при отправке изображения в Telegram.", e);
                    botCommandHandler.sendMessage(chatId, "Ошибка при отправке изображения в Telegram.");
                }
            } else {
                botCommandHandler.sendMessage(chatId, "Ошибка: файл не найден или его кодировка некорректна");
            }
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке изображения.", e);
            botCommandHandler.sendMessage(chatId, "Ошибка при отправке изображения.");
        }
    }
}
