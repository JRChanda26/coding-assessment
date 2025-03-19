package com.interview.interview_service.modal;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "interview_entity")
public class InterviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long interviewId;
    private String userInterviewId;
    private String email;
    private String interviewProfile;
    private String candidateName;
    private String interviewLink;
    private String meetingId;
    private boolean schedulingEmail;
    private boolean isInterviewLinkEmailSent;
    private String interviewStatus;
    private LocalDateTime date;
    private String time;
    private boolean isScheduled;
    private String password;
    private boolean isTokenGenerated;
    private int warningCount;
    @Column(columnDefinition = "LONGTEXT")
    private List<String> imageNames;

    @Column(length = 100000)
    private String authToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "record_id", referencedColumnName = "recordId")
    private RecordEntity interviewRecord;

}
