package com.interview.interview_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.interview.interview_service")

public class InterviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewServiceApplication.class, args);
	}
}
