package org.example;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;

public class Producer implements Callback {
    // Constants for configuration
    private static final Random RND = new Random(0);
    // Using NodePort Service to expose kafka in minikube
    private static final String BOOTSTRAP_SERVERS = "192.168.49.2:32542";
    private static final String TOPIC_NAME = "my-topic";
    private static final long NUM_MESSAGES = 50;
    private static final int MESSAGE_SIZE_BYTES = 100;
    private static final long PROCESSING_DELAY_MS = 1000L;

    protected AtomicLong messageCount = new AtomicLong(0);

    public static void main(String[] args) {
        new Producer().run();
    }

    public void run() {
        System.out.println("Running producer");

        // Create a Kafka producer instance
        // This producer sends messages to the Kafka topic asynchronously
        try (KafkaProducer producer = createKafkaProducer()) {
            // Generate a random byte array as the message payload
            byte[] value = randomBytes(MESSAGE_SIZE_BYTES);
            while (messageCount.get() < NUM_MESSAGES) {
                sleep(PROCESSING_DELAY_MS);
                // Send a message to the Kafka topic, specifying topic name, message key, and message value
                producer.send(new ProducerRecord<>(TOPIC_NAME, messageCount.get(), value), this);
                messageCount.incrementAndGet();
            }
        }
    }

    private KafkaProducer<Long, byte[]> createKafkaProducer() {
        // Create properties for the Kafka producer
        Properties props = new Properties();

        // Configure the connection to Kafka brokers
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // Set a unique client ID for tracking
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "client-" + UUID.randomUUID());

        // Configure serializers for keys and values
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

        return new KafkaProducer<>(props);
    }

    private void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] randomBytes(int size) {
        // Checks the MESSAGE_SIZE_BYTES value is valid
        if (size <= 0) {
            throw new IllegalArgumentException("Record size must be greater than zero");
        }
        byte[] payload = new byte[size];
        for (int i = 0; i < payload.length; ++i) {
            payload[i] = (byte) (RND.nextInt(26) + 65);
        }
        return payload;
    }

    private boolean retriable(Exception e) {
        if (e instanceof IllegalArgumentException
                || e instanceof UnsupportedOperationException
                || !(e instanceof RetriableException)) {
            return false;
        } else {
            return true;
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
}