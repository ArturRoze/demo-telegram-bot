package com.example.demotelegrambot.comand;

public interface Command<T> {

    void execute(T t);
}
