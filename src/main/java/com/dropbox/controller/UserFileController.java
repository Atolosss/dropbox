package com.dropbox.controller;

import com.dropbox.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.gmm.demo.controller.api.UserFileApi;
import ru.gmm.demo.model.api.UserFileRs;
import ru.gmm.demo.model.api.UserFileUploadRq;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFileController implements UserFileApi {

    private final UserFileService userFileService;

    @Override
    public ResponseEntity<List<UserFileRs>> getAllUserFile(final String idUser) {
        return ResponseEntity.ok(userFileService.getAllUserFile(idUser));
    }

    @Override
    public ResponseEntity<UserFileRs> createUserFile(final UserFileUploadRq userFileUploadRq) {
        return ResponseEntity.ok(userFileService.createUserFile(userFileUploadRq));
    }

    @Override
    public ResponseEntity<UserFileRs> getUserFile(final String id) {
        return ResponseEntity.ok(userFileService.getUserFile(id));
    }

    @Override
    public ResponseEntity<Void> deleteUserFile(final String id) {
        userFileService.deleteUserFile(id);
        return ResponseEntity.ok(null);
    }

}
