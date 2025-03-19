package com.interview.interview_service.Recording;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class RecordingServiceImpl implements RecordingService {
    @Autowired
    private RecordingRepo recordingRepo;

    @Override
    public RecordingEntity createRecording(RecordingEntity recordingEntity) {
       return recordingRepo.save(recordingEntity);
    }

    @Override
    public List<RecordingEntity> getAllRecording() {
        return recordingRepo.findAll();
    }

    @Override
    public RecordingEntity getRecordingById(Long id) {
        RecordingEntity recordings = recordingRepo.findById(id).get();
        return recordings;
    }

    @Override
    public RecordingEntity updateRecording(Long id, RecordingEntity recordingEntity) {
      RecordingEntity existingrecord = recordingRepo.findById(id).get();
      if (existingrecord != null) {
      existingrecord.setDownloadUrlExpiry(recordingEntity.getDownloadUrlExpiry());
      existingrecord.setFileSize(recordingEntity.getFileSize());
      existingrecord.setInvokedTime((recordingEntity.getInvokedTime()));
      existingrecord.setOutputFileName(recordingEntity.getOutputFileName());
      existingrecord.setSessionId(recordingEntity.getSessionId());
      existingrecord.setStartedTime(recordingEntity.getStartedTime());
      existingrecord.setStoppedTime(recordingEntity.getStoppedTime());
      existingrecord.setRecordingDuration(recordingEntity.getRecordingDuration());
    RecordingEntity updatedrecord = recordingRepo.save(existingrecord);
    return updatedrecord;
} else {
    return null;
}

    }

    @Override
    public void deleteRecording(Long id) {
       recordingRepo.deleteById(id);
    }

    @Override
    public List<RecordingEntity> getRecordingsByMeetingId(String meetingid) {
        return recordingRepo.findByMeetingid(meetingid);
    }
    
    
}
