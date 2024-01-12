package com.dropbox.bot.support;

import com.dropbox.bot.enums.Buttons;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class Keyboard {
    public static SendMessage hermitageInlineKeyboardAb(final long chatId) {
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите дальнейшие действие:");

        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        final List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(createButton(Buttons.BUTTON_01.getText(), Buttons.BUTTON_01.getCallback()));

        final List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(createButton(Buttons.BUTTON_02.getText(), Buttons.BUTTON_02.getCallback()));
        rowInline1.add(createButton(Buttons.BUTTON_03.getText(), Buttons.BUTTON_03.getCallback()));

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    private static InlineKeyboardButton createButton(final String text, final String callback) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(text);
        keyboardButton.setCallbackData(callback);
        return keyboardButton;
    }
}

