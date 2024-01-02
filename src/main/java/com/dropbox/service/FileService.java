package com.dropbox.service;

import com.dropbox.client.FileStorageClient;
import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.FileMapper;
import com.dropbox.model.dto.UploadFileDtoRq;
import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import com.dropbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final FileStorageClient fileStorageClient;

    public FileRs createFile(final FileUploadRq fileUploadRq) {
        if (!userRepository.existsById(fileUploadRq.getUserId())) {
            throw new ServiceException(ErrorCode.ERR_CODE_001, fileUploadRq.getUserId());
        }

        final UploadFileDtoRq uploadFileDtoRq = UploadFileDtoRq.builder()
            .base64Data(fileUploadRq.getFileData())
            .name(fileUploadRq.getName())
            .userId(fileUploadRq.getUserId())
            .build();

        final UploadFileRs uploadFileRs = fileStorageClient.uploadFile(uploadFileDtoRq);
        return fileMapper.toFileRs(uploadFileRs, fileUploadRq);
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

}
