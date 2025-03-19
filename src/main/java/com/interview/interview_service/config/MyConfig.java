package com.interview.interview_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;


public class MyConfig {

    @Value("${app.timezone}")
    private String appTimeZone;

    @Bean
    public String appSpecificTimeZone() {
        // Logic to create or configure the time zone String (optional)
        // You can return a specific time zone object or a formatted string
        return appTimeZone;// Example
    }
    
}
