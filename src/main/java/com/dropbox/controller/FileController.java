package com.dropbox.controller;

import com.dropbox.controller.openapi.FileApi;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
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
    public ResponseEntity<UploadFileDtoRs> getFile(final String id) {
        return ResponseEntity.ok(fileService.getFile(id));
    }

}
