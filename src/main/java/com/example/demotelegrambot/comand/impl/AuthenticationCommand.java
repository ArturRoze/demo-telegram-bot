package com.example.demotelegrambot.comand.impl;

import com.example.demotelegrambot.comand.Command;
import com.example.demotelegrambot.kafka.producer.TelegramAuthenticationProducer;
import com.example.demotelegrambot.model.dto.TelegramAuthenticateUserDto;
import com.example.demotelegrambot.model.entity.user.TelegramUser;
import com.example.demotelegrambot.repository.TelegramUserRepository;
import com.example.demotelegrambot.sender.MessageSender;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import com.example.demotelegrambot.service.storage.CommandDataStorage;
import com.example.demotelegrambot.service.storage.CommandStateStorage;
import com.example.demotelegrambot.service.storage.CommandStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static com.example.demotelegrambot.service.storage.CommandStates.LOGIN_STATE;
import static com.example.demotelegrambot.service.storage.CommandStates.PASSWORD_STATE;
import static java.util.Collections.singletonList;

@Component
public class AuthenticationCommand implements Command<Long> {

    private final TelegramUserRepository telegramUserRepository;

    private final CommandStateStorage commandStateStorage;

    private final CommandDataStorage commandDataStorage;

    private final MessageSender messageSender;

    private final MessageBundleService messageBundleService;

    private final TelegramAuthenticationProducer telegramAuthenticationProducer;

    @Autowired
    public AuthenticationCommand(TelegramUserRepository telegramUserRepository,
                                 CommandStateStorage commandStateStorage,
                                 CommandDataStorage commandDataStorage,
                                 MessageSender messageSender,
                                 MessageBundleService messageBundleService,
                                 TelegramAuthenticationProducer telegramAuthenticationProducer) {
        this.telegramUserRepository = telegramUserRepository;
        this.commandStateStorage = commandStateStorage;
        this.commandDataStorage = commandDataStorage;
        this.messageSender = messageSender;
        this.messageBundleService = messageBundleService;
        this.telegramAuthenticationProducer = telegramAuthenticationProducer;
    }

    @Override
    public void execute(Long chatId) {

        TelegramUser telegramUser = telegramUserRepository.findByChatId(chatId);

        SendMessage sendMessage = buildSendMessageWithAuthenticationButton(chatId, "sand.message.login", telegramUser.getUserLocale());

        messageSender.send(sendMessage);

    }

    public void doEnterAuthentication(Long chatId) {

        TelegramUser telegramUser = telegramUserRepository.findByChatId(chatId);

        SendMessage sendMessage = buildSendMessage(chatId, "sand.message.login", telegramUser.getUserLocale());

        messageSender.send(sendMessage);

        commandStateStorage.put(chatId, LOGIN_STATE);
    }

    public void doEnterLogin(Message message) {

        commandDataStorage.put(message.getChatId(), LOGIN_STATE, message.getText());

        commandStateStorage.put(message.getChatId(), PASSWORD_STATE);

        Locale locale = Locale.forLanguageTag(message.getFrom().getLanguageCode());

        SendMessage sendMessage = buildSendMessage(message.getChatId(), "sand.message.password", UserLocale.valueOf(locale.getLanguage()));

        messageSender.send(sendMessage);
    }

    public void doEnterPassword(Message message) {

        commandDataStorage.put(message.getChatId(), PASSWORD_STATE, message.getText());

        telegramAuthenticationProducer.sendMessage(buildTelegramAuthenticateUserDto(message.getChatId()));

        commandStateStorage.delete(message.getChatId());
    }

    public SendMessage buildSendMessage(Long chatId, String messageKey, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .parseMode("HTML")
                .text(messageBundleService.findLocaleMessage(messageKey, userLocale).getText())
                .build();
    }

    public SendMessage buildSuccessSendMessage(Long chatId, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .parseMode("HTML")
                .text(messageBundleService.findLocaleMessage("command.success.authenticate.message", userLocale).getText()
                )
                .replyMarkup(buildContactEmailReplyKeyboardMarkup(userLocale))
                .build();
    }

    private ReplyKeyboardMarkup buildContactEmailReplyKeyboardMarkup(UserLocale userLocale) {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Arrays.asList(
                        KeyboardButton.builder().text(messageBundleService.findLocaleMessage("button.contact.command.name", userLocale)
                                .getText()).requestContact(true).build(),
                        KeyboardButton.builder().text(messageBundleService.findLocaleMessage("button.email.command.name", userLocale)
                                .getText()).build()))
                )
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    private SendMessage buildSendMessageWithAuthenticationButton(Long chatId, String messageKey, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .parseMode("HTML")
                .text(messageBundleService.findLocaleMessage(messageKey, userLocale).getText())
                .replyMarkup(buildReplyKeyboardMarkup(userLocale))
                .build();
    }

    private ReplyKeyboardMarkup buildReplyKeyboardMarkup(UserLocale userLocale) {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(singletonList(
                        KeyboardButton.builder().text(messageBundleService.findLocaleMessage("button.authenticate.command.name", userLocale)
                                .getText()).build()))
                )
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    private TelegramAuthenticateUserDto buildTelegramAuthenticateUserDto(Long chatId) {
        Map<CommandStates, String> commandsValue = commandDataStorage.findByKey(chatId);

        TelegramAuthenticateUserDto telegramAuthenticateUserDto = new TelegramAuthenticateUserDto();
        telegramAuthenticateUserDto.setChatId(chatId);
        telegramAuthenticateUserDto.setLogin(commandsValue.get(LOGIN_STATE));
        telegramAuthenticateUserDto.setPassword(commandsValue.get(PASSWORD_STATE));

        return telegramAuthenticateUserDto;
    }
}
