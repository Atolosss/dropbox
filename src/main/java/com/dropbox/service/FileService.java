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

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final FileStorageClient fileStorageClient;

    public FileRs createFile(final UploadFileDtoRq uploadFileDtoRq) {
        if (!userRepository.existsById(uploadFileDtoRq.getUserId())) {
            throw new ServiceException(ErrorCode.ERR_CODE_001, uploadFileDtoRq.getUserId());
        }

        final UploadFileRs uploadFileRs = fileStorageClient.uploadFile(uploadFileDtoRq);
        return fileMapper.toFileRs(uploadFileDtoRq, uploadFileRs);
    }

    public UploadFileDtoRs getFile(final String fileKey) {
        final DataBuffer downloadFile = fileStorageClient.downloadFile(fileKey);
        try (InputStream inputStream = downloadFile.asInputStream()) {
            final String encoded = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
            return fileMapper.toUploadFileDtoRs(encoded);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> getListMetaFiles(final Long chatId) {
        return fileStorageClient.downloadListMetaFiles(chatId).stream().map(UploadFileDtoRs::getName).toList();
    }

}
