package com.dropbox.support;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import lombok.experimental.UtilityClass;
import ru.gmm.demo.model.api.FilePatchRq;
import ru.gmm.demo.model.api.FileUploadRq;
import ru.gmm.demo.model.api.UserPatchRq;
import ru.gmm.demo.model.api.UserRegistrationRq;

import java.util.List;

@UtilityClass
public class DataProvider {
    public static User.UserBuilder prepareUser() {
        return User.builder()
            .email("email")
            .files(List.of());
    }

    public static UserRegistrationRq.UserRegistrationRqBuilder prepareUserRegistrationRq() {
        return UserRegistrationRq.builder()
            .email("email132")
            .password("password");
    }

    public static UserPatchRq.UserPatchRqBuilder prepareUserPatchRq() {
        return UserPatchRq.builder()
            .lastName("max")
            .firstName("komar")
            .dateOfBirth("20.03.1993");
    }

    public static File.FileBuilder prepareFile() {
        return File.builder()
            .name("How make doc");
    }

    public static FileUploadRq.FileUploadRqBuilder prepareFileUploadRq(final String id) {
        return FileUploadRq.builder()
            .name("How make doc")
            .userId(id);
    }

    public static FilePatchRq.FilePatchRqBuilder prepareFilePatchRq() {
        return FilePatchRq.builder()
            .url("google.com");
    }
}
