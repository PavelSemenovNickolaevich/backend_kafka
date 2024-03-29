package io.fraud.tests;

import io.fraud.database.model.Deal;
import io.fraud.kafka.KafkaRecord;
import io.fraud.kafka.consumer.KafkaMessageConsumer;
import io.fraud.kafka.messages.DealMessage;
import io.fraud.kafka.messages.GeneratorMessage;
import io.fraud.kafka.producer.KafkaMessageProducer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BackendTests extends BaseTest {
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
        GeneratorMessage generatorMessage = new GeneratorMessage();
        generatorMessage.setDate(new Date().toString());
        generatorMessage.setAmount(2000);
        generatorMessage.setCurrency("EUR");
        generatorMessage.setSource(RandomStringUtils.randomAlphabetic(10));
        generatorMessage.setTarget(RandomStringUtils.randomAlphabetic(10));


        kafkaService.subscribeLegit();
        //  kafkaService.send("queuing.transactions", "{\"date\": \"01/07/2021 20:01:07\", \"source\": \"Java12\", \"target\": \"python\", \"amount\": 900.0, \"currency\": \"EUR\"}");
        kafkaService.send(generatorMessage);
        // KafkaRecord receivedRecords = kafkaService.waitForMessage("Java12");

        DealMessage dealMessage = kafkaService.waitForMessage(generatorMessage.getSource()).valueAs(DealMessage.class);

        assertThat(dealMessage.getAmount()).isEqualTo(generatorMessage.getAmount());
        assertThat(dealMessage.getBaseCurrency()).isEqualTo("USD");

    }

    @Test
    void testApplicationCanProcessFraudMessage() {
//        GeneratorMessage generatorMessage = new GeneratorMessage();
//        generatorMessage.setDate(new Date().toString());
//        generatorMessage.setAmount(2000);
//        generatorMessage.setCurrency("EUR");
//        generatorMessage.setSource(RandomStringUtils.randomAlphabetic(10));
//        generatorMessage.setTarget(RandomStringUtils.randomAlphabetic(10));


        kafkaService.subscribeFraud();
        //  kafkaService.send("queuing.transactions", "{\"date\": \"01/07/2021 20:01:07\", \"source\": \"Java12\", \"target\": \"python\", \"amount\": 900.0, \"currency\": \"EUR\"}");
        // kafkaService.send(generatorMessage);
        GeneratorMessage generatorMessage = kafkaService.send();
        // KafkaRecord receivedRecords = kafkaService.waitForMessage("Java12");

        DealMessage dealMessage = kafkaService.waitForMessage(generatorMessage.getSource()).valueAs(DealMessage.class);

        assertThat(dealMessage.getAmount()).isEqualTo(2000);
        assertThat(dealMessage.getBaseCurrency()).isEqualTo("USD");

    }

    @Test
    void testApplicationCanSaveFraudMessageToDataBase() {
        Deal deal = dbService.findDealById(185);
        List<Deal> dealList = dbService.findByCurrency("EUR");


        assertThat(deal.getAmount()).isEqualTo(2000);

        assertThat(dealList.size()).isEqualTo(5);

    }
}
