package com.dropbox.controller;

import com.dropbox.controller.openapi.UserApi;
import com.dropbox.model.openapi.UserPatchRq;
import com.dropbox.model.openapi.UserPatchRs;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.model.openapi.UserRegistrationRs;
import com.dropbox.model.openapi.UserRs;
import com.dropbox.service.FileService;
import com.dropbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final FileService fileService;

    @Override
    public ResponseEntity<UserRegistrationRs> createUser(final UserRegistrationRq userRegistrationRq) {
        return ResponseEntity.ok(userService.createUser(userRegistrationRq));
    }

    @Override
    public ResponseEntity<UserPatchRs> patchUser(final Long id, final UserPatchRq userPatchRq) {
        return ResponseEntity.ok(userService.patchUser(id, userPatchRq));
    }

    @Override
    public ResponseEntity<Void> deleteUser(final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(null);

    }

    @Override
    public ResponseEntity<List<UserRs>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserRs> getUser(final Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

}
