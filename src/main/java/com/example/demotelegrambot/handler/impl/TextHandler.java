package com.example.demotelegrambot.handler.impl;

import com.example.demotelegrambot.comand.Commands;
import com.example.demotelegrambot.comand.impl.AuthenticationCommand;
import com.example.demotelegrambot.comand.impl.EmailCommand;
import com.example.demotelegrambot.comand.impl.HelpCommand;
import com.example.demotelegrambot.comand.impl.StartCommand;
import com.example.demotelegrambot.handler.Handler;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.storage.CommandStateStorage;
import com.example.demotelegrambot.service.storage.CommandStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextHandler implements Handler {

    private final MessageBundleService messageBundleService;

    private final CommandStateStorage commandStateStorage;

    private final StartCommand startCommand;

    private final AuthenticationCommand authenticationCommand;

    private final HelpCommand helpCommand;

    private final EmailCommand emailCommand;

    @Autowired
    public TextHandler(MessageBundleService messageBundleService,
                       CommandStateStorage commandStateStorage,
                       StartCommand startCommand,
                       AuthenticationCommand authenticationCommand,
                       HelpCommand helpCommand,
                       EmailCommand emailCommand) {
        this.messageBundleService = messageBundleService;
        this.commandStateStorage = commandStateStorage;
        this.startCommand = startCommand;
        this.authenticationCommand = authenticationCommand;
        this.helpCommand = helpCommand;
        this.emailCommand = emailCommand;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        if (message.getText().startsWith(Commands.START.getCommandText())) {
            startCommand.execute(message);
        } else if (message.getText().startsWith(Commands.HELP.getCommandText())) {
            helpCommand.execute(message.getChatId());
        } else if (messageBundleService.containsByText("button.authenticate.command.name", message.getText())) {
            authenticationCommand.doEnterAuthentication(message.getChatId());
        } else if (messageBundleService.containsByText("button.email.command.name", message.getText())) {
            emailCommand.execute(message);
        } else { // logic if enter button we manage command after enter
            CommandStates commandStates = commandStateStorage.findByKey(message.getChatId());
            if (CommandStates.LOGIN_STATE.equals(commandStates)) {
                authenticationCommand.doEnterLogin(message);
            }
            if (CommandStates.PASSWORD_STATE.equals(commandStates)) {
                authenticationCommand.doEnterPassword(message);
            }
            if (CommandStates.EMAIL_STATE.equals(commandStates)) {
                emailCommand.doEnterEmail(message);
            }
        }
    }
}
