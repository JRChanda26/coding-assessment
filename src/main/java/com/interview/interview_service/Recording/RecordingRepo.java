package com.interview.interview_service.Recording;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordingRepo extends JpaRepository<RecordingEntity,Long> {
    List<RecordingEntity> findByMeetingid(String meetingid);
    
}
