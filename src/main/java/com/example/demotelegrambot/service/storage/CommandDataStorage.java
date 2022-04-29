package com.example.demotelegrambot.service.storage;

import java.util.Map;

public interface CommandDataStorage {

    void put(Long chatId, CommandStates commandState, String value);

    Map<CommandStates, String> findByKey(Long chatId);

    void delete(Long chatId);
}
