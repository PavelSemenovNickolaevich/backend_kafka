package io.fraud.kafka.consumer;

import io.fraud.kafka.KafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

public class KafkaMessageConsumer {

    private static KafkaConsumer<String, String> consumer;

    public Collection<KafkaRecord> getReceivedRecords() {
        return receivedRecords;
    }

    private final Collection<KafkaRecord> receivedRecords = new ArrayList<>();

    //ожидания по времени
    public Collection<KafkaRecord> waitForMessage() {
        Awaitility.await().atMost(30, TimeUnit.SECONDS)
                .until(() -> consume().size() > 0);
        return getReceivedRecords();
    }

    //ожидание до тех пор пока consumer не получит сообщение
    public KafkaRecord waitForMessage(String message) {
        Awaitility.await().atMost(20, TimeUnit.SECONDS)
                .until(() -> consume().stream().anyMatch(it -> it.hasSource(message)));
        return getReceivedRecords().stream().filter(it -> it.hasSource(message))
                .findFirst().orElseThrow(() -> new RuntimeException("No such record with sourceId" + message));
    }

    public KafkaMessageConsumer(String bootStrapServer) {
        consumer = new KafkaConsumer<>(createConsumerProperties(bootStrapServer));
    }

    private Properties createConsumerProperties(String bootStrapServer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    public Collection<KafkaRecord> consume() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(0));
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset  = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

            this.receivedRecords.add(new KafkaRecord(record));
        }
        return receivedRecords;
    }

    public void subscribe(String topic) {
        consumer.subscribe(singletonList(topic));
    }

}
