package com.example.kafkascheduler.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KafkaScheduledConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaScheduledConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "* * * * *") // Run every minute
    public void consumeMessagesForToday() {
        log.info("Starting consumer");
        LocalDate today = LocalDate.now();
        long midnightTimestamp = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long nextMidnightTimestamp = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // create a consumer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("scheduler-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                long messageTimestamp = record.timestamp();
                String headerValue = Arrays.toString(record.headers().lastHeader("scheduler-timestamp").value());
                long schedulerTimestamp = Long.parseLong(headerValue);

                if (schedulerTimestamp >= midnightTimestamp && schedulerTimestamp < nextMidnightTimestamp) {
                    // process message for current date
                    // ...
                    log.info("Message: {}, timestamp: {}", message, Instant.ofEpochMilli(messageTimestamp));
                    //acknowledge message
                    consumer.commitSync();
                }

            }

        }
    }
}
