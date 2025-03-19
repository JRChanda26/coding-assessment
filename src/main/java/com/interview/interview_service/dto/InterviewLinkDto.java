package com.interview.interview_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewLinkDto {

    private String authToken;
    private String meetingId;
    private String email; 
    private String interviewProfile;
    // private String userInterviewId;
}
