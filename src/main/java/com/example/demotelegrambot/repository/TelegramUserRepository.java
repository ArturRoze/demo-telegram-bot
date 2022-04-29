package com.example.demotelegrambot.repository;

import com.example.demotelegrambot.model.entity.user.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    TelegramUser findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}
