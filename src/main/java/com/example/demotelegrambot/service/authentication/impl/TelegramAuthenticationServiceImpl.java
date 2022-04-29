package com.example.demotelegrambot.service.authentication.impl;

import com.example.demotelegrambot.comand.impl.AuthenticationCommand;
import com.example.demotelegrambot.model.dto.TelegramAuthorizedUserDto;
import com.example.demotelegrambot.model.entity.user.TelegramUser;
import com.example.demotelegrambot.repository.TelegramUserRepository;
import com.example.demotelegrambot.sender.MessageSender;
import com.example.demotelegrambot.service.authentication.TelegramAuthenticationService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramAuthenticationServiceImpl implements TelegramAuthenticationService {

    private final MessageSender messageSender;

    private final TelegramUserRepository telegramUserRepository;

    private final AuthenticationCommand authenticationCommand;

    @Autowired
    public TelegramAuthenticationServiceImpl(MessageSender messageSender,
                                             TelegramUserRepository telegramUserRepository,
                                             AuthenticationCommand authenticationCommand) {
        this.messageSender = messageSender;
        this.telegramUserRepository = telegramUserRepository;
        this.authenticationCommand = authenticationCommand;
    }

    @Override
    public void processAuthentication(TelegramAuthorizedUserDto telegramAuthorizedUserDto) {

        Long chatId = telegramAuthorizedUserDto.getChatId();
        TelegramUser telegramUser = telegramUserRepository.findByChatId(chatId);
        UserLocale userLocale = telegramUser.getUserLocale();

        Boolean authenticated = telegramAuthorizedUserDto.getAuthenticated();

        if (authenticated == null || !authenticated) {
            messageSender.send(authenticationCommand.buildSendMessage(chatId, "command.failure.authenticate.message", userLocale));
            authenticationCommand.execute(chatId);
        } else {
            telegramUser.setUserId(telegramAuthorizedUserDto.getUserId());
            telegramUser.setLogin(telegramAuthorizedUserDto.getLogin());
            telegramUserRepository.save(telegramUser);

            messageSender.send(authenticationCommand.buildSuccessSendMessage(chatId, userLocale));
        }
    }

}
