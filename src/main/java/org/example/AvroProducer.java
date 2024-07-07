package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;
import org.example.generated.Payment;
import org.example.utils.KafkaConfig;

public class AvroProducer implements Callback {

    private static final String TOPIC = "transactions";

    public static void main(String[] args) throws InterruptedException {

        KafkaProducer<String, Payment> producer = KafkaConfig.createKafkaProducer();

            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final Payment payment = new Payment(orderId, 1000.00d);
                final ProducerRecord<String, Payment> record = new ProducerRecord<String, Payment>(TOPIC, payment.getId().toString(), payment);
                producer.send(record);
                Thread.sleep(1000L);
            }
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if (e != null) {
            // If an exception occurred while sending the record
            System.err.println(e.getMessage());

            if (!retriable(e)) {
                // If the exception is not retriable, print the stack trace and exit
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            // If the record was successfully sent
            System.out.printf("Record sent to %s-%d with offset %d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        }

    }
    private boolean retriable(Exception e) {
        return e instanceof RetriableException;
    }
}
