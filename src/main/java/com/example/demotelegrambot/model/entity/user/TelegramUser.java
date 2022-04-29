package com.example.demotelegrambot.model.entity.user;

import com.example.demotelegrambot.service.bundle.UserLocale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "telegram_user")
public class TelegramUser implements Serializable {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "telegram_user_id_seq")
    @SequenceGenerator(name = "telegram_user_id_seq", sequenceName = "telegram_user_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "login", unique = true)
    private String login;

    @Enumerated(STRING)
    @Column(name = "user_locale")
    private UserLocale userLocale;

    public TelegramUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserLocale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(UserLocale userLocale) {
        this.userLocale = userLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramUser that = (TelegramUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(chatId, that.chatId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(login, that.login) &&
                userLocale == that.userLocale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, userId, login, userLocale);
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userId=" + userId +
                ", login='" + login + '\'' +
                ", userLocale=" + userLocale +
                '}';
    }
}
