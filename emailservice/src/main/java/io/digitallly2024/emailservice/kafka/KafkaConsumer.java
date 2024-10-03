package io.digitallly2024.emailservice.kafka;

import io.digitallly2024.emailservice.handler.ResetPasswordMessageHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ResetPasswordMessageHandler resetPasswordMessageHandler;

    @Autowired
    public KafkaConsumer(ResetPasswordMessageHandler resetPasswordMessageHandler) {
        this.resetPasswordMessageHandler = resetPasswordMessageHandler;
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {"${spring.kafka.topics.reset-password-topic}"})
    public void devTestTopicListener(ConsumerRecord<String, String> consumerRecord) {
        LOGGER.info("Reading message from topic: {}, partition: {}, offset: {}}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        resetPasswordMessageHandler.handleMessage(consumerRecord);
    }
}
