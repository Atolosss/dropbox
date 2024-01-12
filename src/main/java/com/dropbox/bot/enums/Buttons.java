package com.dropbox.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Buttons {
    BUTTON_01("Сообщить о проблеме", "ПРОБЛЕМА"),
    BUTTON_02("Список обращений", "ОБРАЩЕНИЕ"),
    BUTTON_03("Список фотографий", "ФОТО");

    private final String text;
    private final String callback;
}
