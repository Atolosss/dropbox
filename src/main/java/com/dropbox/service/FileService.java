package com.dropbox.service;

//import com.dropbox.mapper.FileMapper;
//import com.dropbox.model.entity.File;
//import com.dropbox.model.entity.User;
//import com.dropbox.repository.FileRepository;
//import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
//import ru.gmm.demo.model.api.FilePatchRq;
//import ru.gmm.demo.model.api.FileRs;
//import ru.gmm.demo.model.api.FileUploadRq;
//
//import java.util.List;

@Service
@AllArgsConstructor
public class FileService {

//    private final FileRepository fileRepository;
//    private final FileMapper fileMapper;
//    private final UserRepository userRepository;
//
//    public FileRs createFile(final FileUploadRq fileUploadRq) {
//        userRepository.findById(fileUploadRq.getUserId())
//            .orElseThrow();
//
//        final File file = fileMapper.toFile(fileUploadRq);
//        fileRepository.save(file);
//        return fileMapper.toFileRs(file);
//    }
//
//    public FileRs getFile(final Long id) {
//        final File file = fileRepository.findById(id)
//            .orElseThrow();
//        return fileMapper.toFileRs(file);
//    }
//
//    public void deleteFile(final Long id) {
//        fileRepository.deleteById(id);
//    }
//
//    public FileRs patchFile(final Long id, final FilePatchRq userFilePatchRq) {
//        final File file = fileRepository.findById(id)
//            .orElseThrow();
//
//        fileMapper.update(file, userFilePatchRq);
//
//        final File saved = fileRepository.save(file);
//        return fileMapper.toFileRs(saved);
//    }
//
//    public List<FileRs> getUserFiles(final String id) {
//        final User user = userRepository.findById(id).orElseThrow();
//        return user.getFiles().stream().map(fileMapper::toFileRs).toList();
//    }
}
