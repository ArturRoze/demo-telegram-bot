package com.example.demotelegrambot.kafka.consumer;

import com.example.demotelegrambot.model.dto.TelegramAuthorizedUserDto;
import com.example.demotelegrambot.service.authentication.TelegramAuthenticationService;
import com.example.demotelegrambot.util.JsonParserUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TelegramAuthenticationConsumer {

    private final Logger logger = getLogger(TelegramAuthenticationConsumer.class);

    private final TelegramAuthenticationService telegramAuthenticationService;

    @Autowired
    public TelegramAuthenticationConsumer(TelegramAuthenticationService telegramAuthenticationService) {
        this.telegramAuthenticationService = telegramAuthenticationService;
    }

    @KafkaListener(groupId = "telegram_authenticate_group",
            topicPattern = "${spring.kafka.prefix.telegram-authenticate-topic}"
//            errorHandler = "kafkaConsumerErrorHandler"
    )
    public void telegramAuthenticateListener(ConsumerRecord<Long, String> record) {
        TelegramAuthorizedUserDto telegramAuthorizedUserDto = JsonParserUtils.convertToObject(record.value(), TelegramAuthorizedUserDto.class);

        telegramAuthenticationService.processAuthentication(telegramAuthorizedUserDto);
    }

}
