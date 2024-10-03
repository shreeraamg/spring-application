package io.digitallly2024.emailservice.handler;

import io.digitallly2024.emailservice.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitallly2024.commonlib.domain.message.ResetPasswordMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordMessageHandler.class);

    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    @Autowired
    public ResetPasswordMessageHandler(EmailService emailService) {
        this.emailService = emailService;
        this.objectMapper = new ObjectMapper();
    }

    public void handleMessage(ConsumerRecord<String, String> consumerRecord) {
        try {
            ResetPasswordMessage resetPasswordMessage = objectMapper.readValue(consumerRecord.value(), ResetPasswordMessage.class);
            LOGGER.info("Message parsed successfully from topic: {}, partition: {}, offset: {}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
            emailService.sendResetPasswordEmail(resetPasswordMessage.getEmail(), resetPasswordMessage.getToken());
        } catch (JsonProcessingException ex) {
            LOGGER.error("Failed to parse message from topic: {}, partition: {}, offset: {}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        }
    }



}
