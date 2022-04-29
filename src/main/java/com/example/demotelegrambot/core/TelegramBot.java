package com.example.demotelegrambot.core;

import com.example.demotelegrambot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final Set<Handler> handlers;

    @Autowired
    public TelegramBot(Set<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<Handler> supportedHandlers = handlers.stream()
                .filter(handler -> handler.supports(update))
                .collect(toList());

        if (supportedHandlers.size() > 1) {
            throw new IllegalArgumentException("We find more than one handler");
        }
        if ( supportedHandlers.size() == 0 ) {
            throw new UnsupportedOperationException("Unknown update");
        }

        supportedHandlers.get(0).handle(update);
    }
}
