package io.fraud.tests;

import io.fraud.kafka.KafkaService;
import io.fraud.kafka.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    //protected static KafkaService kafkaService;
    protected final KafkaService kafkaService = new KafkaService();

//    @BeforeAll
//    static void setUp() {
//        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
//        kafkaService = new KafkaService(projectConfig.kafkaBrokers());
//    }
}
