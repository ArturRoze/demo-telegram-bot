package com.example.demotelegrambot.model.dto;

public class TelegramAuthorizedUserDto {

    private Long userId;

    private Long chatId;

    private String login;

    private Boolean authenticated;

    public TelegramAuthorizedUserDto() {
    }

    public TelegramAuthorizedUserDto(Long userId, Long chatId, String login, Boolean authenticated) {
        this.userId = userId;
        this.login = login;
        this.authenticated = authenticated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public String toString() {
        return "TelegramAuthorizedUserDto{" +
                "userId=" + userId +
                ", chatId=" + chatId +
                ", login='" + login + '\'' +
                ", authenticated=" + authenticated +
                '}';
    }
}
