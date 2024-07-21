package org.example;

import org.apache.kafka.clients.consumer.*;
import org.example.generated.Payment;
import org.example.utils.KafkaConfig;

import java.time.Duration;
import java.util.Collections;


public class AvroConsumer {

    private static final String TOPIC = "transactions";

    public static void main(String[] args) {

        try (KafkaConsumer<String, Payment> consumer = KafkaConfig.createKafkaConsumer()) {

            Duration timeout = Duration.ofMillis(100);
            consumer.subscribe(Collections.singletonList(TOPIC));
            int counter = 0;
            while (true) {
                ConsumerRecords<String, Payment> records = consumer.poll(timeout);
                for (ConsumerRecord<String, Payment> record : records) {
                    System.out.println(record.value().toString());
                    counter++;

                    if (counter == 9) {
                        break;
                    }

                }
            }
        }
    }
}
