package com.interview.interview_service.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.interview_service.commonservice.MailService;
import com.interview.interview_service.dto.InterviewDto;
import com.interview.interview_service.dto.InterviewLinkDto;
import com.interview.interview_service.dto.InterviewLoginResponseDto;
import com.interview.interview_service.modal.InterviewEntity;
import com.interview.interview_service.repository.InterviewRepository;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class InterviewService {

    @Autowired
    InterviewRepository interviewRepository;

    @Autowired
    private MailService MailService;

    public InterviewEntity dataAdding(InterviewEntity interviewEntity) {
        String generateInterviewId = UUID.randomUUID().toString();
        String upToNCharacters = generateInterviewId.substring(5, 15);

        String generatePassword = UUID.randomUUID().toString();
        String password = generatePassword.substring(2, 15);
        interviewEntity.setPassword(password);
        interviewEntity.setUserInterviewId(upToNCharacters);
        interviewEntity.setScheduled(false);
        InterviewEntity data = interviewRepository.save(interviewEntity);

        try {
            String emailContent = String.format("<html>\r\n" +
                    "<body>\r\n" +
                    "<p style=\"font-size:16px; color:#000000; \">Dear Candidate, <br/></p>\r\n" +
                    "<p style=\"font-size:16px; color:#000000; \">I hope this email finds you well.</p>\r\n" +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "I am pleased to inform you that you have successfully passed the screening round. Congratulations on this achievement! "
                    +
                    "Your qualifications, experiences, and enthusiasm for the role truly stood out to us.<br/></p>\r\n"
                    +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "As we move forward in the selection process, we would like to invite you to the next round of assessments. "
                    +
                    "This next stage will provide us with an opportunity to delve deeper into your skills, experiences, and how they align with our team's needs.<br/></p>\r\n"
                    +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "Please find attached a calendar link that will allow you to schedule a time for your next interview. "
                    +
                    "During this session, you will have the chance to meet with [interviewer(s)] and further discuss your background, qualifications, and how you can contribute to our team.<br/></p>\r\n"
                    +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "Should you have any questions regarding the next steps or need any assistance, please feel free to reach out to me at [Your Contact Information]. "
                    +
                    "We are here to support you throughout this process.<br/></p>\r\n" +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "Once again, congratulations on your progression to the next round. We are excited to continue our discussions and get to know you better.<br/></p>\r\n"
                    +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "Looking forward to speaking with you soon!<br/></p>\r\n" +
                    "<p style=\"font-size:20px; color:#000000; \">Candidate Interview Id: "
                    + interviewEntity.getUserInterviewId() + "</p>\r\n" +

                    "<p style=\"font-size:20px; color:#000000; \">Candidate Password : "
                    + interviewEntity.getPassword() + "</p>\r\n" +
                    "<p style=\"font-size:16px; color:#000000; \">" +
                    "Please find the link to schedule your interview:</p>\r\n" +
                    "<h4 style=\"color:#000000; cursor: pointer;\"><a href=\"https://aippoint.ai/aippoint-userinterface/logincandidate\" style=\"font-size:16px; color:#000000; \">https://aippoint.ai/aippoint-userinterface/scheduleinterview</a></h4>\r\n"
                    +
                    "<p style=\"font-size:16px; color:#000000; \"><br/><br/>Thank you,<br/>Team Parseez</p>\r\n" +
                    "</body>\r\n" +
                    "</html>\r\n");
            MailService.sendEmail(interviewEntity.getEmail(),
                    "[Parseez] Please confirm your email address to schedule interview", emailContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<InterviewEntity> GetAllData() {
        List<InterviewEntity> data = interviewRepository.findAll();
        return data;
    }

    // public String updateData(InterviewDto interviewDto) {
    // try {

    // Optional<InterviewEntity> entity = interviewRepository
    // .findByUserInterviewId(interviewDto.getUserInterviewId());
    // InterviewEntity collected = entity.get();
    // if (collected != null) {
    // DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy
    // hh:mm a")
    // .withZone(ZoneId.of("UTC"));
    // DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy
    // HH:mm:ss");
    // LocalDateTime dateTime = LocalDateTime.parse(interviewDto.getDate() + " " +
    // interviewDto.getTime(),
    // inputFormatter);

    // System.out.println("inputFormatter " + inputFormatter);
    // // String formattedDateTime = dateTime.format(outputFormatter);
    // collected.setDate(dateTime);
    // collected.setTime(interviewDto.getTime());
    // collected.setScheduled(true);
    // interviewRepository.save(collected);
    // return "interview schedule successful";
    // } else {
    // throw new EntityNotFoundException("Data does not Exists");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "invalid credentials";
    // }
    // }

    public String updateData(InterviewDto interviewDto) {
        try {
            Optional<InterviewEntity> entity = interviewRepository
                    .findByUserInterviewId(interviewDto.getUserInterviewId());

            if (entity.isPresent()) {
                InterviewEntity collected = entity.get();

                // Define the input formatter to match the expected input format (four-digit
                // year)
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-uu hh:mm a", Locale.ENGLISH)
                        .withResolverStyle(ResolverStyle.STRICT);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                // Combine date and time strings
                String dateTimeString = interviewDto.getDate() + " " + interviewDto.getTime();

                // Debug print
                System.out.println("Combined DateTime String: " + dateTimeString);

                // Parse combined date and time into LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, inputFormatter);

                // Optional: format the parsed date if needed
                String formattedDateTime = dateTime.format(outputFormatter);
                System.out.println("Formatted DateTime: " + formattedDateTime);

                // Set the parsed datetime to the entity
                collected.setDate(dateTime); // Assuming date includes both date and time
                collected.setScheduled(true);

                // Save the updated entity
                interviewRepository.save(collected);

                return "Interview schedule successful";
            } else {
                throw new EntityNotFoundException("Data does not exist");
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "Invalid date or time format. Please ensure it's in 'dd-MM-yy hh:mm a' format.";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while scheduling the interview.";
        }
    }

    public InterviewEntity creatingEntity(InterviewEntity interviewEntity) {

        String generatePassword = UUID.randomUUID().toString();
        String password = generatePassword.substring(2, 15);
        interviewEntity.setPassword(password);
        InterviewEntity data = interviewRepository.save(interviewEntity);
        return data;
    }

    public InterviewLoginResponseDto candidateLogin(String email, String password) {
        InterviewLoginResponseDto dto = new InterviewLoginResponseDto();

        try {
            Optional<InterviewEntity> optionalCandidate = interviewRepository.findByEmailAndPassword(email, password);

            if (optionalCandidate.isPresent()) {
                InterviewEntity existingCandidate = optionalCandidate.get();
                dto.setUserInterviewId(existingCandidate.getUserInterviewId());
                dto.setScheduled(existingCandidate.isScheduled());
            } else {
                dto.setMessage("No candidate found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dto.setMessage("An error occurred during login");
        }

        return dto;
    }

    public InterviewLinkDto sendInterviewlink(String userInterviewId) {

        Optional<InterviewEntity> data = interviewRepository.findByUserInterviewId(userInterviewId);
        InterviewEntity entity = data.get();
        InterviewLinkDto dto = new InterviewLinkDto();
        dto.setAuthToken(entity.getAuthToken());
        dto.setEmail(entity.getEmail());
        dto.setMeetingId(entity.getMeetingId());
        dto.setInterviewProfile(entity.getInterviewProfile());
        return dto;
    }

    public InterviewEntity updateUserImage(String imagePath, String userInterviewId) {
        InterviewEntity existingCandidate = interviewRepository.findByUserInterviewId(userInterviewId).get();
        if (existingCandidate != null) {
            // Get the existing list of image names or create a new one if it's null
            List<String> imageNames = existingCandidate.getImageNames();
            if (imageNames == null) {
                imageNames = new ArrayList<>();
            }
            // Add the new image name to the list
            imageNames.add(imagePath);
            // Update the image names in the user entity
            existingCandidate.setImageNames(imageNames);
            // Save the updated user entity
            InterviewEntity updatedCandidate = interviewRepository.save(existingCandidate);
            return updatedCandidate;
        }
        return null;
    }

    public void updateWarningCount(long interviewId, int warningCount) {
        InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview with ID " + interviewId + " not found."));

        // Update only the warning count
        interviewEntity.setWarningCount(warningCount);

        // Save the updated entity
        interviewRepository.save(interviewEntity);
    }

    public boolean isMaxWarningsReached(long interviewId) {
        Optional<InterviewEntity> interviewEntity = interviewRepository.findById(interviewId);
        if (interviewEntity.isPresent()) {
            return interviewEntity.get().getWarningCount() > 3;
        }
        throw new RuntimeException("Interview with ID " + interviewId + " not found.");
    }

}
