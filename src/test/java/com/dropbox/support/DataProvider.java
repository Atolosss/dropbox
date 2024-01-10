package com.dropbox.support;

import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UserRegistrationRq;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {
    public static User.UserBuilder<?, ?> prepareUser() {
        return User.builder()
            .telegramUserId(232L)
            .lastName("komar")
            .firstName("max");
    }

    public static UserRegistrationRq.UserRegistrationRqBuilder prepareUserRegistrationRq() {
        return UserRegistrationRq.builder()
            .telegramUserId(232L)
            .lastName("komar")
            .firstName("max");
    }

    public static UploadFileDtoRq.UploadFileDtoRqBuilder prepareFileUploadRq(final Long id) {
        return UploadFileDtoRq.builder()
            .name("How make doc")
            .base64Data("123")
            .userId(id);
    }

}
