package com.dropbox.support;

import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UserPatchRq;
import com.dropbox.model.openapi.UserRegistrationRq;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {
    public static User.UserBuilder<?, ?> prepareUser() {
        return User.builder()
            .email("email");
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

    public static FileUploadRq.FileUploadRqBuilder prepareFileUploadRq(final Long id) {
        return FileUploadRq.builder()
            .name("How make doc")
            .userId(id);
    }

}
