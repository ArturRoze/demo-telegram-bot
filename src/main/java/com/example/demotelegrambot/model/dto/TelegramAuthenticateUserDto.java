package com.example.demotelegrambot.model.dto;

public class TelegramAuthenticateUserDto {

    private Long chatId;

    private String login;

    private String password;

    public TelegramAuthenticateUserDto() {
    }

    public TelegramAuthenticateUserDto(Long chatId, String login, String password) {
        this.chatId = chatId;
        this.login = login;
        this.password = password;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TelegramAuthenticateUserDto{" +
                "chatId=" + chatId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
