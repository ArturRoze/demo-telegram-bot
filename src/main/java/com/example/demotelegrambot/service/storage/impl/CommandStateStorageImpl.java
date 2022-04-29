package com.example.demotelegrambot.service.storage.impl;

import com.example.demotelegrambot.service.storage.CommandStateStorage;
import com.example.demotelegrambot.service.storage.CommandStates;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class CommandStateStorageImpl implements CommandStateStorage {

    private final Map<Long, CommandStates> chatIdToState = new ConcurrentHashMap<>();

    @Override
    public void put(Long chatId, CommandStates commandStates) {
        chatIdToState.put(chatId, commandStates);
    }

    @Override
    public CommandStates findByKey(Long chatId) {
        return chatIdToState.get(chatId);
    }

    @Override
    public void delete(Long chatId) {
        chatIdToState.remove(chatId);
    }

}
