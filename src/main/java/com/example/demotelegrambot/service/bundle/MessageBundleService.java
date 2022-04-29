package com.example.demotelegrambot.service.bundle;


import java.util.List;

public interface MessageBundleService {

    LocalisedMessage findLocaleMessage(String key, UserLocale userLocale);

    List<String> findAllMessages(String key);

    boolean containsByText(String key, String text);
}
