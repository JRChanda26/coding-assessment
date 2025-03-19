package com.interview.interview_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewLoginResponseDto {
    
    private String userInterviewId;
    private boolean isScheduled;
    private String message;
}
