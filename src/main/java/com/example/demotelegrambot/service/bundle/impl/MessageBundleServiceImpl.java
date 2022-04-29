package com.example.demotelegrambot.service.bundle.impl;

import com.example.demotelegrambot.service.bundle.LocalisedMessage;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MessageBundleServiceImpl implements MessageBundleService {

    private static final Locale EN = new Locale("en", "US");
    private static final Locale UA = new Locale("uk", "UA");
    private static final Locale RU = new Locale("ru", "RU");

    private final MessageSource messageSource;

    @Autowired
    public MessageBundleServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public LocalisedMessage findLocaleMessage(String key, UserLocale userLocale) {
        return new LocalisedMessage(key, messageSource.getMessage(key, new Object[]{}, findLocal(userLocale)));
    }

    @Override
    public List<String> findAllMessages(String key) {
        List<String> allMessages = new ArrayList<>();

        allMessages.add(messageSource.getMessage(key, new Object[]{}, EN));
        allMessages.add(messageSource.getMessage(key, new Object[]{}, UA));
        allMessages.add(messageSource.getMessage(key, new Object[]{}, RU));

        return allMessages;
    }

    @Override
    public boolean containsByText(String key, String text) {
        List<String> allMessages = findAllMessages(key);

        return allMessages.stream().anyMatch(localeText -> localeText.equals(text));
    }

    private Locale findLocal(UserLocale userLocale){
        if (UserLocale.ru.equals(userLocale)) {
            return RU;
        }
        if (UserLocale.uk.equals(userLocale)) {
            return UA;
        }

        return EN;
    }
}
