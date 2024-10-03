package io.digitallly2024.webservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitallly2024.commonlib.domain.message.ResetPasswordMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class KafkaProducer {

    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${spring.kafka.topics.reset-password-topic}")
    private String devTestTopic;

    private final KafkaTemplate<String, String> kafkaResetPasswordMessageTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaResetPasswordMessageTemplate, ObjectMapper objectMapper) {
        this.kafkaResetPasswordMessageTemplate = kafkaResetPasswordMessageTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTestMessage(ResetPasswordMessage message) {
        try {
            String jsonString = objectMapper.writeValueAsString(message);
            Future<SendResult<String, String>> sendResultFuture = kafkaResetPasswordMessageTemplate.send(devTestTopic, jsonString);
            SendResult<String, String> sendResult = sendResultFuture.get();
            RecordMetadata metadata = sendResult.getRecordMetadata();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Sent message to topic: {}, partition: {}, offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
            }
            kafkaResetPasswordMessageTemplate.flush();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            Thread.currentThread().interrupt();
        }

    }

}
