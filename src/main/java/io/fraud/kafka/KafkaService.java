package io.fraud.kafka;

import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.producer.KafkaMessageProducer;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaService {
    private final KafkaMessageProducer kafkaMessageProducer;
    private final KafkaMessageConsumer messageConsumer;

    public KafkaMessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public KafkaService(String server) {
        this.kafkaMessageProducer = new KafkaMessageProducer(server);
        this.kafkaMessageProducer.createProducer();
        this.messageConsumer = new KafkaMessageConsumer(server);
    }

    public RecordMetadata send(String topic, String message) {
        return kafkaMessageProducer.send(topic, message);
    }

    public RecordMetadata send(String message) {
        return send("test", message);
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
