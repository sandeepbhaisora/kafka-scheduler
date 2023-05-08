package com.example.kafkascheduler.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleRequest {

    private String message;
    private LocalDateTime scheduledTime;

}
