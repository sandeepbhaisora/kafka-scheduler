//package com.example.kafkascheduler.consumer;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.listener.ConsumerSeekAware;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Map;
//
//@Component
//public class KafkaMessageConsumer implements ConsumerSeekAware {
//
//    private final String topic = "scheduler-topic";
//    private final int partition = 0;
//    private final String dateFormat = "yyyy-MM-dd";
//    private LocalDate currentDate = LocalDate.now();
//    private long midnightTimestamp;
//
//    @KafkaListener(topicPartitions = {@TopicPartition(topic = topic, partitions = {partition})})
//    public void consumeMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
//        String message = record.value();
//        long messageTimestamp = record.timestamp();
//
//        if (messageTimestamp >= midnightTimestamp) {
//            // Process message for current date
//            // ...
//
//            acknowledgment.acknowledge();
//        }
//        else {
//            // Skip message for previous date
//        }
//    }
//
//    @Override
//    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//        midnightTimestamp = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//        callback.seekToBeginning(topic, partition);
//
//        // Seek to the beginning of the partition for the current day
//        callback.seek(topic, partition, midnightTimestamp);
//    }
//
//
//}
