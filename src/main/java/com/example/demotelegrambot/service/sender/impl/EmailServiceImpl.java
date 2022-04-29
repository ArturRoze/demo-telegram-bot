package com.example.demotelegrambot.service.sender.impl;

import com.example.demotelegrambot.service.sender.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(String to, String subject, String text, File attachment) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(attachment);
                helper.addInline(attachment.getName(), file);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(mimeMessage);
    }
}
