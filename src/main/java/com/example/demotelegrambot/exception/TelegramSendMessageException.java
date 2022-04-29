package com.example.demotelegrambot.exception;

public class TelegramSendMessageException extends RuntimeException{

    public TelegramSendMessageException(String message) {
        super(message);
    }

    public TelegramSendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
