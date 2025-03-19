package com.interview.interview_service.Recording;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;

// import com.example.internalservice.Interview.InterViewEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@CrossOrigin(origins = "https://aippoint.ai/aippoint-spring-interviewservice", maxAge = 3600)
@Entity(name = "recording_master")
public class RecordingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String meetingid;
    private LocalDateTime downloadUrlExpiry;
    private Long fileSize;
    private String sessionId;
    @Column(length = 4000)
    private String outputFileName;
    private LocalDateTime invokedTime;
    private LocalDateTime startedTime;
    private LocalDateTime stoppedTime;
    private int recordingDuration;

    // @ManyToMany(mappedBy = "recording")
    // Set<InterViewEntity> recording= new HashSet<>();
}
