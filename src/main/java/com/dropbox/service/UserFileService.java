package com.dropbox.service;

import com.dropbox.mapper.UserFileMapper;
import com.dropbox.model.entity.User;
import com.dropbox.model.entity.UserFile;
import com.dropbox.repository.UserFileRepository;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gmm.demo.model.api.UserFileRs;
import ru.gmm.demo.model.api.UserFileUploadRq;

import java.util.List;

@Service
@AllArgsConstructor
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private final UserFileMapper userFileMapper;
    private final UserRepository userRepository;

    public List<UserFileRs> getAllUserFile(final String idUser) {
        final List<UserFile> userFiles = userRepository.findById(idUser).get().getFiles();
        return userFileMapper.toUserFileRsList(userFiles);
    }

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
}
