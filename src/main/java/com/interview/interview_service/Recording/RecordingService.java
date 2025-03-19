package com.interview.interview_service.Recording;

import java.util.List;

public interface RecordingService {
    public RecordingEntity createRecording(RecordingEntity recordingEntity);
    public List<RecordingEntity> getAllRecording();
    public RecordingEntity getRecordingById(Long id);
    public RecordingEntity updateRecording(Long id, RecordingEntity recordingEntity);
    void deleteRecording(Long id);
    List<RecordingEntity> getRecordingsByMeetingId(String meetingid);
    
}
