package org.example.utils;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.generated.Payment;

import java.util.Properties;
import java.util.UUID;

public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "192.168.49.2:30663";
    private static final String SCHEMA_REGISTRY_URL = "http://192.168.49.2:30007";
    private static final String SSL_SECURITY_PROTOCOL = "SSL";
    private static final String SSL_TRUSTSTORE_LOCATION = "/tmp/client.truststore.p12";
    private static final String SSL_TRUSTSTORE_PASSWORD = "123456";
    private static final String ACKS_CONFIG = "all";
    private static final String PRODUCER_INTERCEPTOR_CLASS = "org.example.interceptors.CountingProducerInterceptor";

    // Consumer specific configs
    private static final String GROUP_ID = "avro-group";

    public static KafkaProducer<String, Payment> createKafkaProducer() {
        // Create properties for the Kafka producer
        Properties props = new Properties();
        // Configure the connection to Kafka brokers
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        // Set a unique client ID for tracking
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "client-" + UUID.randomUUID());

        // Configure serializers for keys and values
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG);
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, PRODUCER_INTERCEPTOR_CLASS);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        // Schema registry config
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);

        // SSL config
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SSL_TRUSTSTORE_LOCATION);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SSL_TRUSTSTORE_PASSWORD);
        props.put("security.protocol", SSL_SECURITY_PROTOCOL);

        return new KafkaProducer<>(props);
    }

    public static KafkaConsumer<String, Payment> createKafkaConsumer() {

        Properties props = new Properties();

        // Configure the connection to Kafka brokers
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // Set a unique client ID for tracking
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "client-" + UUID.randomUUID());

        // Set a consumer group ID for the consumer
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        // Configure deserializers for keys and values
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        // Disable automatic offset committing
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        // Set the offset reset behavior to start consuming from the earliest available offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Schema registry config
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);

        // SSL configs
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SSL_TRUSTSTORE_LOCATION);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SSL_TRUSTSTORE_PASSWORD);
        props.put("security.protocol", SSL_SECURITY_PROTOCOL);

        return new KafkaConsumer<>(props);

    }
}
