package com.example.kafkascheduler.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(String topic, String message, String timestamp) {
        log.info("Pushing message to Kafka");

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, Long.valueOf(timestamp), null, message);
        record.headers().add(new RecordHeader("scheduler-timestamp", timestamp.getBytes()));

        kafkaTemplate.send(record);
    }

}
