package com.example.demotelegrambot.comand.impl;

import com.example.demotelegrambot.comand.Command;
import com.example.demotelegrambot.model.entity.user.TelegramUser;
import com.example.demotelegrambot.repository.TelegramUserRepository;
import com.example.demotelegrambot.sender.MessageSender;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Component
public class StartCommand implements Command<Message> {

    private final TelegramUserRepository telegramUserRepository;

    private final AuthenticationCommand authenticationCommand;

    private final MessageSender messageSender;

    private final MessageBundleService messageBundleService;

    @Autowired
    public StartCommand(TelegramUserRepository telegramUserRepository,
                        AuthenticationCommand authenticationCommand,
                        MessageSender messageSender,
                        MessageBundleService messageBundleService) {
        this.telegramUserRepository = telegramUserRepository;
        this.authenticationCommand = authenticationCommand;
        this.messageSender = messageSender;
        this.messageBundleService = messageBundleService;
    }

    @Override
    public void execute(Message message) {

        Locale locale = Locale.forLanguageTag(message.getFrom().getLanguageCode());
        UserLocale userLocale = UserLocale.valueOf(locale.getLanguage());

        if (!telegramUserRepository.existsByChatId(message.getChatId())) {
            telegramUserRepository.save(buildTelegramUser(message.getChatId(), userLocale));
        }

        SendMessage sendMessage = buildSendMessage(message, userLocale);

        messageSender.send(sendMessage);

        authenticationCommand.execute(message.getChatId());
    }

    private TelegramUser buildTelegramUser(Long chatId, UserLocale userLocale) {

        TelegramUser telegramUser = new TelegramUser();

        telegramUser.setChatId(chatId);
        telegramUser.setUserLocale(userLocale);

        return telegramUser;
    }

    private SendMessage buildSendMessage(Message message, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .parseMode("HTML")
                .text(messageBundleService.findLocaleMessage("command.start.message", userLocale)
                        .applyPlaceholder("NAME", message.getFrom().getFirstName())
                        .getText()
                )
                .build();
    }

}
