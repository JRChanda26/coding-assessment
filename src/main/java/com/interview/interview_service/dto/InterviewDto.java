package com.interview.interview_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDto {

    private String date;
    private String time;
    private String userInterviewId;

}
