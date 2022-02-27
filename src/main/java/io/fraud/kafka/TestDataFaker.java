package io.fraud.kafka;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDataFaker {

    public String date() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public String  source() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public String  target() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public String  currency() {
        return "EUR";
    }

    public String  amount() {
        return "900";
    }
}
