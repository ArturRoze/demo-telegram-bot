package com.example.demotelegrambot.kafka.producer;

import com.example.demotelegrambot.model.dto.TelegramAuthenticateUserDto;
import com.example.demotelegrambot.util.JsonParserUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TelegramAuthenticationProducer {

    private final Logger logger = getLogger(TelegramAuthenticationProducer.class);

    @Value("${spring.kafka.prefix.telegram-message-topic}")
    private String telegramMessageTopic;

    private final KafkaTemplate<Long, String> kafkaTemplate;

    public TelegramAuthenticationProducer(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(TelegramAuthenticateUserDto telegramAuthenticateUserDto) {
        String message = JsonParserUtils.convertToJson(telegramAuthenticateUserDto);
        Long chatId = telegramAuthenticateUserDto.getChatId();
        ListenableFuture<SendResult<Long, String>> futureObject = kafkaTemplate.send(telegramMessageTopic, chatId, message);
        futureObject.addCallback(getCallback(message, telegramMessageTopic));
    }

    private ListenableFutureCallback<? super SendResult<Long, String>> getCallback(String data, String kafkaTopic) {
        return new ListenableFutureCallback<SendResult<Long, String>>() {

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                logger.info("TELEGRAM MESSAGE: Data was sent to topic " + kafkaTopic + ": " + "successfully");
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("TELEGRAM MESSAGE: Error while sending data " + data + " to topic " + kafkaTopic);
                throw new RuntimeException(ex.getMessage());
            }

        };
    }

}
