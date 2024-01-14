package com.dropbox.bot.handlers;

import com.dropbox.controller.UserController;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class UserRegistrationHandler {
    private final UserRepository userRepository;
    private final UserController userController;

    public void registrationUser(final Message msg) {
        if (userRepository.findUserByTelegramUserId(msg.getChatId()) == null) {
            var userId = msg.getFrom().getId();
            var chat = msg.getChat();

            userController.createUser(UserRegistrationRq.builder()
                    .telegramUserId(userId)
                    .firstName(chat.getFirstName())
                    .lastName(chat.getLastName())
                    .build());
        }
    }
}
