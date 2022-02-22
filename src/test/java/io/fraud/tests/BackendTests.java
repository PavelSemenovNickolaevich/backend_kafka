package io.fraud.tests;

import io.fraud.kafka.KafkaRecord;
import io.fraud.kafka.KafkaService;
import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.messages.DealMessage;
import io.fraud.kafka.producer.KafkaMessageProducer;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class BackendTests {

    private final KafkaService kafkaService = new KafkaService("localhost:9092");

 //to do записать попробоваль на удаленный адрес
    @Test
    void testCanWriteMessageToQueingTransaction() {
        KafkaMessageProducer kafkaMessageProducer = new KafkaMessageProducer("localhost:9092");
        kafkaMessageProducer.createProducer();

        KafkaMessageConsumer messageConsumer = new KafkaMessageConsumer("localhost:9092");
        messageConsumer.subscribe("test");
        messageConsumer.consume();

        kafkaMessageProducer.send("test", "Terminator T-10001");

        KafkaRecord receivedRecords = messageConsumer.waitForMessage("Terminator T-1000");

        System.out.println(receivedRecords);
        assertThat(receivedRecords).isNotNull();

    }

    @Test
    void anotherVariantTestCanWriteMessageToQueingTransaction() {

        kafkaService.subscribe("test");
        kafkaService.send("test", "Terminator T-10002");

        KafkaRecord receivedRecords = kafkaService.waitForMessage("Terminator T-10002");
        assertThat(receivedRecords).isNotNull();

    }

    @Test
    void testApplicationCanProcessValidMessage() {
        kafkaService.subscribe("streaming.transactions.legit");
    //    kafkaService.createProducer();
        kafkaService.send("queuing.transactions", "{\"date\": \"01/07/2021 20:01:07\", \"source\": \"Java12\", \"target\": \"1234444\", \"amount\": 900.0, \"currency\": \"EUR\"}");
        KafkaRecord receivedRecords = kafkaService.waitForMessage("Java12");
 //       assertThat(receivedRecords).isNotNull();
//        DealMessage dealMessage = receivedRecords.valueAs(DealMessage.class);
//
//        assertThat(dealMessage.getAmount()).isEqualTo(900.0);

    }
}
