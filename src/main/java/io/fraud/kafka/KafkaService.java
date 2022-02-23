package io.fraud.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.producer.KafkaMessageProducer;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaService {
    private final KafkaMessageProducer kafkaMessageProducer;
    private final KafkaMessageConsumer messageConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public KafkaService() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        this.kafkaMessageProducer = new KafkaMessageProducer(projectConfig.kafkaBrokers());
        this.kafkaMessageProducer.createProducer();
        this.messageConsumer = new KafkaMessageConsumer(projectConfig.kafkaBrokers());
    }

    public RecordMetadata send(String topic, String message) {
        return kafkaMessageProducer.send(topic, message);
    }

    public RecordMetadata send(String message) {
        return send("test", message);
    }

    @SneakyThrows
    public RecordMetadata send(String topic, Object message) {
        return send(topic,  objectMapper.writeValueAsString(message));
    }

    public void subscribe(String topic) {
        messageConsumer.subscribe(topic);
        messageConsumer.consume();
    }

    public KafkaRecord waitForMessage(String message) {
        return messageConsumer.waitForMessage(message);
    }

    public void createProducer() {
        kafkaMessageProducer.createProducer();
    }
}
