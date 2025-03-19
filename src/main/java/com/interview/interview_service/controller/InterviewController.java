package com.interview.interview_service.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.interview.interview_service.commonservice.FileStorageService;
import com.interview.interview_service.dto.InterviewDto;
import com.interview.interview_service.dto.InterviewLinkDto;
import com.interview.interview_service.dto.InterviewLoginResponseDto;
import com.interview.interview_service.modal.InterviewEntity;
import com.interview.interview_service.repository.InterviewRepository;
import com.interview.interview_service.service.InterviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "https://aippoint.ai/aippoint-userinterface/", maxAge = 3600)
@RequestMapping("/interview")
public class InterviewController {

    private final FileStorageService fileStorageService;
    @Autowired
    InterviewService interviewService;

    @PostMapping
    public ResponseEntity<?> dataAdding(@RequestBody InterviewEntity InterviewEntity) {
        InterviewEntity data = interviewService.dataAdding(InterviewEntity);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getAllInterviewDetails() {
        List<InterviewEntity> data = interviewService.GetAllData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // @PostMapping("/update")
    // public ResponseEntity<?> updateData(@RequestBody InterviewEntity
    // interviewEntity, @RequestParam long interviewId) {
    // InterviewEntity data = interviewService.update(interviewEntity, interviewId);
    // return new ResponseEntity<>(data, HttpStatus.OK);
    // }

    @PostMapping("/updatetime")
    public ResponseEntity<?> updatedTime(@RequestBody InterviewDto interviewDto) {

        String data = interviewService.updateData(interviewDto);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/sendemail")
    public ResponseEntity<?> creatingRecord(@RequestBody InterviewEntity interviewEntity) {
        InterviewEntity data = interviewService.creatingEntity(interviewEntity);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/logincandidate")
    public ResponseEntity<?> candidateLogin(@RequestParam String email,
            @RequestParam String password) {
        InterviewLoginResponseDto data = interviewService.candidateLogin(email, password);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @GetMapping("/gettingdetails")
    public ResponseEntity<?> sendInterviewLinkDetails(@RequestParam String userInterviewId) {
        InterviewLinkDto data = interviewService.sendInterviewlink(userInterviewId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @Autowired
    private InterviewRepository interviewRepository;

    @PostMapping(path = "/write/fileupload/media/url")
    public ResponseEntity<String> uploadFileFromUrl(@RequestParam String userInterviewId,
            @RequestBody Map<String, String> requestParams) {
        // String token = request.getHeader("Authorization");
        // token = token.replace("Bearer ", "");
        // FirebaseToken decodedToken = firebaseAuthService.verifyToken(token);
        // String userEmail = decodedToken.getEmail();

        // Extract fileUrl and meetingId from the request body

        // InterviewEntity data =
        // interviewRepository.findByUserInterviewId(userInterviewId).get();
        // InterviewEntity entity = data.get();
        // InterviewLinkDto dto = new InterviewLinkDto();
        // dto.setAuthToken(entity.getAuthToken());
        // dto.setEmail(entity.getEmail());
        // dto.setMeetingId(entity.getMeetingId());
        // dto.setInterviewProfile(entity.getInterviewProfile());

        String fileUrl = requestParams.get("fileUrl");
        String meetingId = requestParams.get("meetingId");

        // Download and store the file
        String storedFilePath = fileStorageService.downloadAndStoreFile(fileUrl, meetingId);

        InterviewEntity updatedCandidate = interviewService.updateUserImage(storedFilePath, userInterviewId);
        if (updatedCandidate != null) {
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{interviewId}/update-warning-count")
    public ResponseEntity<String> updateWarningCount(
            @PathVariable long interviewId,
            @RequestBody int warningCount) {
        try {
            // Call the service to update warning count
            interviewService.updateWarningCount(interviewId, warningCount);
            return ResponseEntity.ok("Warning count updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{interviewId}/check-warnings")
    public ResponseEntity<String> checkWarningCount(@PathVariable long interviewId) {
        boolean isMaxWarningsReached = interviewService.isMaxWarningsReached(interviewId);
        if (isMaxWarningsReached) {
            return ResponseEntity.ok("Maximum warning count reached.");
        } else {
            return ResponseEntity.ok("Warning count is within the limit.");
        }
    }
}
