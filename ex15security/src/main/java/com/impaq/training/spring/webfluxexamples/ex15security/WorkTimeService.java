package com.impaq.training.spring.webfluxexamples.ex15security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkTimeService {

    private final WorkTimeProperties workTimeProperties;

    public boolean isBusinessHour(){
        //fake implementations, return value from property file
        return workTimeProperties.getBusinessHours();
    }


    @ConfigurationProperties("work.time")
    @Data
    public static class WorkTimeProperties{
        private Boolean businessHours;
    }
}
