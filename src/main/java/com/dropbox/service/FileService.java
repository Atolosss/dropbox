package com.dropbox.service;

import com.dropbox.client.FileStorageClient;
import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.FileMapper;
import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final FileRepository fileRepository;
    private final FileStorageClient fileStorageClient;

    @Transactional
    public FileRs createFile(final UploadFileDtoRq uploadFileDtoRq) {
        User user = userRepository.findUserByTelegramUserId(uploadFileDtoRq.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, uploadFileDtoRq.getUserId()));

        final UploadFileRs uploadFileRs = fileStorageClient.uploadFile(uploadFileDtoRq);
        fileRepository.save(fileMapper.toUserFile(uploadFileDtoRq, uploadFileRs, user));
        List<UserFile> files = user.getFiles();
        files.add(fileMapper.toUserFile(uploadFileDtoRq, uploadFileRs, user));
        userRepository.save(user);
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

}
