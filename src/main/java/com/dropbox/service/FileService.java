package com.dropbox.service;

import com.dropbox.client.FileStorageClient;
import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.FileMapper;
import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import com.dropbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final FileStorageClient fileStorageClient;
    private final FileHelperService fileHelperService;

    public FileRs createFile(final UploadFileDtoRq uploadFileDtoRq) {
        userRepository.findUserByTelegramUserId(uploadFileDtoRq.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, uploadFileDtoRq.getUserId()));

        final UploadFileRs uploadFileRs = fileStorageClient.uploadFile(uploadFileDtoRq);

        fileHelperService.saveFile(uploadFileDtoRq, uploadFileRs);
        return fileMapper.toFileRs(uploadFileDtoRq, uploadFileRs);
    }

    public UploadFileDtoRs downloadFile(final String fileKey) {
        final DataBuffer downloadFile = fileStorageClient.downloadFile(fileKey);
        try (InputStream inputStream = downloadFile.asInputStream()) {
            final String encoded = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
            return fileMapper.toUploadFileDtoRs(encoded);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String uploadFile(final Message message, byte[] fileContent) {
        final String encodedFile = Base64.getEncoder().encodeToString(fileContent);

        final UploadFileDtoRq uploadFileDtoRq = UploadFileDtoRq.builder()
            .name(message.getCaption())
            .userId(message.getFrom().getId())
            .base64Data(encodedFile)
            .build();
        if (message.getCaption() != null) {
            final FileRs fileRs = createFile(uploadFileDtoRq);
            log.info(fileRs.toString());
            return "Ваше обращение принято, могу ли я вам еще чем нибудь помочь? Напишите в чат да или нет";
        } else {
            return "Требуется описать проблему!";
        }

    }



}
