package io.fraud.tests;

import io.fraud.kafka.KafkaRecord;
import io.fraud.kafka.KafkaService;
import io.fraud.kafka.messages.DealMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BackendTests {

    private final KafkaService kafkaService = new KafkaService("localhost:9092");


    @Test
    void testCnWriteMessageToQueingTransaction() {
//        KafkaMessageProducer kafkaMessageProducer = new KafkaMessageProducer("localhost:9092");
//        KafkaMessageConsumer messageConsumer = new KafkaMessageConsumer("localhost:9092");
//        messageConsumer.subscribe("T");
//        messageConsumer.consume();

        kafkaService.subscribe("T");
//        kafkaService.consume();
        kafkaService.createProducer();
        kafkaService.send("T", "Hello From Paul");

        KafkaRecord receivedRecords = kafkaService.waitForMessage("Hello From Paul");

        //  System.out.println(receivedRecords);
        assertThat(receivedRecords).isNotNull();

    }

    @Test
    void testApplicationCanProcessValidMessage() {
        kafkaService.subscribe("streaming.transactions.legit");
        kafkaService.createProducer();
        kafkaService.send("queuing.transactions", "{\"date\": \"01/07/2021 20:01:07\", \"source\": \"Java12\", \"target\": \"1234444\", \"amount\": 900.0, \"currency\": \"EUR\"}");
        KafkaRecord receivedRecords = kafkaService.waitForMessage("Java12");
        assertThat(receivedRecords).isNotNull();
        DealMessage dealMessage = receivedRecords.valueAs(DealMessage.class);

        assertThat(dealMessage.getAmount()).isEqualTo(900.0);

    }
}
