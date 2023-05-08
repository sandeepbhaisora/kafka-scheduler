package com.example.kafkascheduler.contoller;

import com.example.kafkascheduler.dto.ScheduleRequest;
import com.example.kafkascheduler.dto.ScheduleResponse;
import com.example.kafkascheduler.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@Slf4j
public class ScheduleController {

    private final KafkaProducer kafkaProducer;

    @Autowired
    public ScheduleController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponse> scheduleMessage(@RequestBody ScheduleRequest request) {
        try {
            log.info("Request received: {}", request);
            String message = request.getMessage();
            long timestamp = request.getScheduledTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            kafkaProducer.sendMessage("scheduler-topic", message, Long.toString(timestamp));
            ScheduleResponse response = new ScheduleResponse("Message scheduled successfully", timestamp);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ScheduleResponse response = new ScheduleResponse("Error scheduling message: " + e.getMessage(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
