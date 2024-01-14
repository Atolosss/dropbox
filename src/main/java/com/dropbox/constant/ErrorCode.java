package com.dropbox.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ERR_CODE_001("ERR.CODE.001", "There is already a user with this id %s"),
    ERR_CODE_002("ERR.CODE.002", "User with this id %s not found"),
    ERR_CODE_003("ERR.CODE.003", "Ошибка при выполнении команды /start");

    private final String code;
    private final String description;

    public String formatDescription(final Object... args) {
        return String.format(description, args);
    }
}
