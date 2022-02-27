package io.fraud.kafka;


import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.HashMap;

public class TestDataGenerator {

     private JtwigModel model;

     public TestDataGenerator() {
         this.model = JtwigModel.newModel()
                 .with("faker", new TestDataFaker());
     }

     public String generate(String path) {
         return generate(path, new HashMap<>());
     }

    private <K, V> String generate(String path, HashMap<String, String> params) {
         params.forEach((k, v) -> {
             model.with(k, v);
         });

        JtwigTemplate template = JtwigTemplate.classpathTemplate(path);
        return template.render(model);
    }

}
