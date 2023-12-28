package com.dropbox.service;

import com.dropbox.client.FileStorageClient;
import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.FileMapper;
import com.dropbox.model.dto.UploadFileDtoRq;
import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.FilePatchRq;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final FileStorageClient fileStorageClient;

    private static UploadFileDtoRq getUploadFileDtoRq(final FileUploadRq fileUploadRq) {
        return UploadFileDtoRq.builder()
            .base64Data(fileUploadRq.getFileData())
            .name(fileUploadRq.getName())
            .build();
    }

    //TODO: нельзя делать синхронные запросы в транзакции!!!!!!!
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public FileRs createFile(final FileUploadRq fileUploadRq) {
        final UploadFileRs uploadFile = fileStorageClient.uploadFile(getUploadFileDtoRq(fileUploadRq));

        final File file = userRepository.findById(fileUploadRq.getUserId())
            .map(user -> fileMapper.toFile(fileUploadRq, user, uploadFile.getKey()))
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, fileUploadRq.getUserId()));

        fileRepository.save(file);

        return fileMapper.toFileRs(file);
    }

    public UploadFileDtoRs getFile(final Long id) {
        final File file = fileRepository.findById(id)
            .orElseThrow();
        final String encoded = Base64.getEncoder().encodeToString(fileStorageClient.downloadFile(file.getKey()));

        return fileMapper.toUploadFileDtoRs(file, encoded);
    }

    public void deleteFile(final Long id) {
        fileRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public FileRs patchFile(final Long id, final FilePatchRq userFilePatchRq) {
        final File file = fileRepository.findById(id)
            .orElseThrow();

        return fileMapper.toFileRs(fileRepository.save(fileMapper.update(file, userFilePatchRq)));
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<FileRs> getUserFiles(final Long id) {
        final User user = userRepository.findById(id).orElseThrow();
        return user.getFiles().stream().map(fileMapper::toFileRs).toList();
    }
}
