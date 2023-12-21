package com.dropbox.controller;

import com.dropbox.controller.openapi.FileApi;
import com.dropbox.model.openapi.FilePatchRq;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final FileService fileService;

    @Override
    public ResponseEntity<FileRs> createFile(final FileUploadRq fileUploadRq) {
        return ResponseEntity.ok(fileService.createFile(fileUploadRq));
    }

    @Override
    public ResponseEntity<Void> deleteFile(final Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<FileRs> getFile(final Long id) {
        return ResponseEntity.ok(fileService.getFile(id));
    }

    @Override
    public ResponseEntity<FileRs> patchFile(final Long id, final FilePatchRq filePatchRq) {
        return ResponseEntity.ok(fileService.patchFile(id, filePatchRq));
    }
}
