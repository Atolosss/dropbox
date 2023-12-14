package com.dropbox.controller;

import com.dropbox.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.gmm.demo.controller.api.FileApi;
import ru.gmm.demo.model.api.FilePatchRq;
import ru.gmm.demo.model.api.FileRs;
import ru.gmm.demo.model.api.FileUploadRq;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final FileService fileService;

    @Override
    public ResponseEntity<FileRs> createFile(final FileUploadRq fileUploadRq) {
        return ResponseEntity.ok(fileService.createFile(fileUploadRq));
    }

    @Override
    public ResponseEntity<Void> deleteFile(final String id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<FileRs> getFile(final String id) {
        return ResponseEntity.ok(fileService.getFile(id));
    }

    @Override
    public ResponseEntity<FileRs> patchFile(final String id, final FilePatchRq filePatchRq) {
        return ResponseEntity.ok(fileService.patchFile(id, filePatchRq));
    }
}
