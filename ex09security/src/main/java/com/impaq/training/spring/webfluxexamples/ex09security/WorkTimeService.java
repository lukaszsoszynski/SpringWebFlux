package com.impaq.training.spring.webfluxexamples.ex09security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkTimeService {

    private final WorkTimeProperties workTimeProperties;

    public boolean isBusinessHour(){
        return workTimeProperties.getBusinessHours();
    }


    @ConfigurationProperties("work.time")
    @Data
    public static class WorkTimeProperties{
        private Boolean businessHours;
    }
}
