package com.example.kafkascheduler.dto;

import lombok.Data;

@Data
public class ScheduleResponse {

    private final String message;
    private final long scheduledTime;

}

