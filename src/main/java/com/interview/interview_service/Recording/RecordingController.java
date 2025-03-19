package com.interview.interview_service.Recording;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("recording")
public class RecordingController {
    @Autowired

    private RecordingService recordingService;

    @CrossOrigin(origins = "https://aippoint.ai/aippoint-spring-interviewservice")
    @PostMapping("/write")
    public ResponseEntity<RecordingEntity> createRecording(@RequestBody RecordingEntity recordingEntity) {
        RecordingEntity recording = recordingService.createRecording(recordingEntity);
        return new ResponseEntity<RecordingEntity>(recording, HttpStatus.CREATED);
    }

    @GetMapping("/read")
    public ResponseEntity<List<RecordingEntity>> getAllRecording() {
        List<RecordingEntity> getrecording = recordingService.getAllRecording();
        return new ResponseEntity<List<RecordingEntity>>(getrecording, HttpStatus.OK);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<RecordingEntity> getRecordingById(@PathVariable Long id) {
        RecordingEntity getrecordingById = recordingService.getRecordingById(id);
        return new ResponseEntity<RecordingEntity>(getrecordingById, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<RecordingEntity> updateRecording(@PathVariable Long id,
            @RequestBody RecordingEntity recordingEntity) {
        RecordingEntity updatedrecording = recordingService.updateRecording(id, recordingEntity);
        return new ResponseEntity<RecordingEntity>(updatedrecording, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInterView(@PathVariable Long id) {
        recordingService.deleteRecording(id);
        return new ResponseEntity<String>("Deleted Recording Successfully..!!", HttpStatus.OK);
    }

    @CrossOrigin(origins = "https://aippoint.ai/aippoint-spring-interviewservice")
    @GetMapping("/meeting/{meetingid}")
    public ResponseEntity<List<RecordingEntity>> getRecordingsByMeetingId(@PathVariable String meetingid) {
        List<RecordingEntity> recordings = recordingService.getRecordingsByMeetingId(meetingid);
        return new ResponseEntity<>(recordings, HttpStatus.OK);
    }

}
