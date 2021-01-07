package io.fraud.kafka;

import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.producer.KafkaMessageProducer;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaService {
    private final KafkaMessageProducer kafkaMessageProducer;

    public KafkaMessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    private final KafkaMessageConsumer messageConsumer;

    public KafkaService(String server) {
        this.kafkaMessageProducer = new KafkaMessageProducer(server);
        this.messageConsumer = new KafkaMessageConsumer(server);
    }

    public RecordMetadata send(String topic, String message) {
        return kafkaMessageProducer.send(topic, message);
    }

    public RecordMetadata send(String message) {
        return send("T", message);
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
