package com.example.demotelegrambot.service.authentication;

import com.example.demotelegrambot.model.dto.TelegramAuthorizedUserDto;

public interface TelegramAuthenticationService {

    void processAuthentication(TelegramAuthorizedUserDto telegramAuthorizedUserDto);
}
