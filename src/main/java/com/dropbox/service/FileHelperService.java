package com.dropbox.service;

import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.FileMapper;
import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.repository.FileRepository;
import com.dropbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileHelperService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileMapper fileMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFile(final UploadFileDtoRq uploadFileDtoRq, final UploadFileRs uploadFileRs) {
        User user = userRepository.findUserByTelegramUserId(uploadFileDtoRq.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, uploadFileDtoRq.getUserId()));
        UserFile userFile = fileMapper.toUserFile(uploadFileDtoRq, uploadFileRs, user);
        fileRepository.save(userFile);
    }
}
