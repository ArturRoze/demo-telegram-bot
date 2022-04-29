package com.example.demotelegrambot.comand.impl;

import com.example.demotelegrambot.comand.Command;
import com.example.demotelegrambot.sender.MessageSender;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import com.example.demotelegrambot.service.sender.EmailService;
import com.example.demotelegrambot.service.storage.CommandStateStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import static com.example.demotelegrambot.service.storage.CommandStates.EMAIL_STATE;

@Component
public class EmailCommand implements Command<Message> {

    private final CommandStateStorage commandStateStorage;

    private final MessageSender messageSender;

    private final MessageBundleService messageBundleService;

    private final EmailService emailService;

    @Autowired
    public EmailCommand(CommandStateStorage commandStateStorage,
                        MessageSender messageSender,
                        MessageBundleService messageBundleService,
                        EmailService emailService) {
        this.commandStateStorage = commandStateStorage;
        this.messageSender = messageSender;
        this.messageBundleService = messageBundleService;
        this.emailService = emailService;
    }

    @Override
    public void execute(Message message) {

        Locale locale = Locale.forLanguageTag(message.getFrom().getLanguageCode());

        SendMessage sendMessage = buildSendMessage(message, "sand.message.email", UserLocale.valueOf(locale.getLanguage()));

        commandStateStorage.put(message.getChatId(), EMAIL_STATE);

        messageSender.send(sendMessage);

    }

    public void doEnterEmail(Message message) {

        Locale locale = Locale.forLanguageTag(message.getFrom().getLanguageCode());
        UserLocale userLocale = UserLocale.valueOf(locale.getLanguage());

        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:picture/image.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        emailService.sendMessage(
                message.getText(),
                "LOVE YOU BABY",
                messageBundleService.findLocaleMessage("sand.message.email.letter", userLocale)
                        .applyPlaceholder("NAME", message.getFrom()
                                .getFirstName())
                        .getText(),
                file
        );

        commandStateStorage.delete(message.getChatId());

        SendMessage sendMessage = buildSendMessage(message, "sand.message.email.response", userLocale);
        messageSender.send(sendMessage);
    }

    private SendMessage buildSendMessage(Message message, String messageKey, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .parseMode("HTML")
                .text(messageBundleService.findLocaleMessage(messageKey, userLocale).getText())
                .build();
    }

}
