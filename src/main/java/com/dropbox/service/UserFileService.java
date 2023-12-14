package com.dropbox.service;

import com.dropbox.mapper.UserFileMapper;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.repository.UserFileRepository;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gmm.demo.model.api.UserFilePatchRq;
import ru.gmm.demo.model.api.UserFilePatchRs;
import ru.gmm.demo.model.api.UserFileRs;
import ru.gmm.demo.model.api.UserFileUploadRq;

import java.util.List;

@Service
@AllArgsConstructor
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private final UserFileMapper userFileMapper;
    private final UserRepository userRepository;

    public UserFileRs createUserFile(final UserFileUploadRq userFileUploadRq) {
        final User user = userRepository.findById(userFileUploadRq.getUser()).orElseThrow();
        final List<UserFile> userFiles = user.getFiles();
        userFiles.add(userFileMapper.toUserFile(userFileUploadRq));
        user.setFiles(userFiles);
        final UserFile insert = userFileRepository.save(userFileMapper.toUserFile(userFileUploadRq));
        userRepository.save(user);
        return userFileMapper.toUserFileRs(insert);
    }

    public UserFileRs getUserFile(final String id) {
        final UserFile userFile = userFileRepository.findById(id).orElseThrow();
        return userFileMapper.toUserFileRs(userFile);

    }

    public void deleteUserFile(final String id) {
        userFileRepository.findById(id).orElseThrow();
        userFileRepository.deleteById(id);
    }

    public List<UserFileRs> getAllUserFile(final String id) {
        final List<UserFile> userFilesList = userRepository.findById(id).get().getFiles();
        return userFileMapper.toUserFileRsList(userFilesList);
    }

    public UserFilePatchRs patchUserFile(final String id, final UserFilePatchRq userFilePatchRq) {
        final UserFile userFile = userFileRepository.findById(id).orElseThrow();
        userFileMapper.update(userFile, userFilePatchRq);
        final UserFile saved = userFileRepository.save(userFile);
        return userFileMapper.toUserFilePatchRs(saved);
    }
}
