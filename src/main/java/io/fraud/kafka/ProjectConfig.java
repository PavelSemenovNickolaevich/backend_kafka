package io.fraud.kafka;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.Config.Sources;


@Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {
    String app();
    @Key("${app}.dbHost")
    String dbHost();
    @Key("${app}.dbPort")
    String dbPort();
    @Key("${app}.dbName")
    String dbName();
    @Key("${app}.dbUser")
    String dbUser();
    @Key("${app}.dbPassword")
    String dbPassword();
    @Key("${app}.kafkaBrokers")
    String kafkaBrokers();
}
