package com.example.demotelegrambot.service.storage.impl;

import com.example.demotelegrambot.service.storage.CommandDataStorage;
import com.example.demotelegrambot.service.storage.CommandStates;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class CommandDataStorageImpl implements CommandDataStorage {

    private final Map<Long, Map<CommandStates, String>> chatIdToCommandStateValues = new ConcurrentHashMap<>();

    @Override
    public void put(Long chatId, CommandStates commandState, String value) {
        chatIdToCommandStateValues.computeIfAbsent(chatId, v -> new HashMap<>())
                .put(commandState, value);
    }

    @Override
    public Map<CommandStates, String> findByKey(Long chatId) {
        return new HashMap<>(chatIdToCommandStateValues.get(chatId));
    }

    @Override
    public void delete(Long chatId) {
        chatIdToCommandStateValues.remove(chatId);
    }
}
