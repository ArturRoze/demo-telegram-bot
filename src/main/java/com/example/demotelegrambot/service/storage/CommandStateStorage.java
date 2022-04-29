package com.example.demotelegrambot.service.storage;

public interface CommandStateStorage {

    void put(Long chatId, CommandStates commandStates);

    CommandStates findByKey(Long chatId);

    void delete(Long chatId);
}
