package com.example.demotelegrambot.service.sender;

import java.io.File;

public interface EmailService {

    void sendMessage(String to, String subject, String text, File attachment);
}
