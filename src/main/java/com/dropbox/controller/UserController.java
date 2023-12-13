package com.dropbox.controller;

import com.dropbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.gmm.demo.controller.api.UserApi;
import ru.gmm.demo.model.api.UserPatchRq;
import ru.gmm.demo.model.api.UserPatchRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;
import ru.gmm.demo.model.api.UserRs;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserRegistrationRs> createUser(final UserRegistrationRq userRegistrationRq) {
        return ResponseEntity.ok(userService.createUser(userRegistrationRq));
    }

    @Override
    public ResponseEntity<UserPatchRs> patchUser(final String id, final UserPatchRq userPatchRq) {
        return ResponseEntity.ok(userService.patchUser(id, userPatchRq));
    }

    @Override
    public ResponseEntity<Void> deleteUser(final String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(null);

    }

    @Override
    public ResponseEntity<List<UserRs>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserRs> getUser(final String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}