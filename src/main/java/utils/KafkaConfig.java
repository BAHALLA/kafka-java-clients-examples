package utils;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.Payment;

import java.util.Properties;
import java.util.UUID;

public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "192.168.49.2:32648";
    private static final String SCHEMA_REGISTRY_URL =  "http://192.168.49.2:31574";
    private static final String SSL_SECURITY_PROTOCOL = "SSL";
    private static final String SSL_TRUSTSTORE_LOCATION = "/tmp/client.truststore.p12";
    private static final String SSL_TRUSTSTORE_PASSWORD = "123456";


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
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SSL_TRUSTSTORE_LOCATION);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SSL_TRUSTSTORE_PASSWORD);
        props.put("security.protocol", SSL_SECURITY_PROTOCOL);

        return new KafkaProducer<>(props);
    }
}
