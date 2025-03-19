package com.interview.interview_service.cronservice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.interview.interview_service.commonservice.MailService;
import com.interview.interview_service.modal.InterviewEntity;
import com.interview.interview_service.repository.InterviewRepository;
import com.interview.interview_service.service.InterviewLinkGeneration;
import com.interview.interview_service.service.InterviewService;

import jakarta.mail.MessagingException;

@Service
public class CronService {

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private MailService MailService;

    // @Autowired
    // private String appSpecificTimeZone;

    @Value("${app.timezone}") // This injects the value of 'app.timezone' from application.yml
    private String appSpecificTimeZone;

    private InterviewLinkGeneration interviewLinkGeneration;

    @Autowired
    public CronService(InterviewLinkGeneration interviewLinkGeneration) {
        this.interviewLinkGeneration = interviewLinkGeneration;
    }

    InterviewEntity interviewEntity = new InterviewEntity();

    @Scheduled(cron = "*/5 * * * * *")
    public void sendInterviewLink() {
        List<InterviewEntity> data = interviewService.GetAllData();

        for (InterviewEntity interview : data) {
            if (!interview.isSchedulingEmail()) {
                String email = interview.getEmail();

                try {
                    String emailContent = String.format(
                            "<html>\r\n" +
                                    "<body>\r\n" +
                                    "<div style=\"background-color:#e0e0e0; padding:10px 100px 30px 100px; text-align:center; font-family:Georgia,serif; margin:auto; width:500px;\">\r\n"
                                    +
                                    "  <div style=\"color:blue; font-size:25px; padding-bottom:10px; font-family:Georgia,serif;\">Parseez</div>\r\n"
                                    +
                                    "  <div style=\"background-color:#fff; padding:15px; text-align:left;\">\r\n" +
                                    // " <div style=\"text-align:center; margin-bottom:15px;\">\r\n" +
                                    // " <img
                                    // src=\"https://parseez.com/parseez-spring-interviewservice/images/emailopen.jpeg\"
                                    // alt=\"open email\"
                                    // style=\"width:150px; height:150px;\" />\r\n" +
                                    // " </div>\r\n" +
                                    "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:20px;\">Dear Candidate,</p>"
                                    +
                                    "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px;\">Please find the below credentials to get logged in to schedule interview.</p>\r\n"
                                    +
                                    "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px;\">Link: &nbsp;"
                                    +
                                    "      <a href=\"https://aippoint.ai/aippoint-userinterface/coding_login\" style=\"font-size:14px; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px; color:blue;\">https://aippoint.ai/aippoint-userinterface/coding_login</a>"
                                    +
                                    "    </p>\r\n" +
                                    "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6;\">Password: <span style=\"color:blue;\">%s</span></p>\r\n"
                                    +
                                    "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6;\"><br/><br/>Thank you,<br/>Team Parseez</p>\r\n"
                                    +
                                    "  </div>\r\n" +
                                    "</div>\r\n" +
                                    "</body>\r\n" +
                                    "</html>\r\n",

                            interview.getPassword());
                    try {
                        MailService.sendEmail(
                                email,
                                "[Parseez] Please find login credentials to schedule interview",
                                emailContent);
                        interview.setSchedulingEmail(true);

                        interviewRepository.save(interview);

                    } catch (Error e) {
                        e.getMessage();

                    }

                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                System.out.println(interview);
            }
        }
    }

    // @Scheduled(cron = "*/10 * * * * *")
    // public void sendInterviewLinkForTheUser() {
    // try {

    // List<InterviewEntity> dataAvaliable = interviewRepository
    // .findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentFalse();
    // System.out.println("syed yasar");
    // // System.out.println(dataAvaliable);
    // System.out.println("syed yasar");
    // for (InterviewEntity interview1 : dataAvaliable) {
    // if (interview1.isScheduled() && interview1.isSchedulingEmail()
    // && !interview1.isInterviewLinkEmailSent()) {
    // System.out.println("im here please tell my name");
    // interview1.setInterviewLinkEmailSent(true);
    // System.out.println("i have changed the status");
    // // System.out.println((ChronoUnit.HOURS.between(interview1.getDate(),
    // // LocalDateTime.now())));
    // String meetingId = interviewLinkGeneration.createMeeting(interview1).block();
    // if (meetingId != null) {
    // System.out.println("im inside the cron still ");
    // interviewRepository.save(interview1);
    // System.out.println("im inside the cron and i have saved the data");

    // } else {
    // System.out.println("the meeting id is not yet generated");
    // }
    // interviewLinkGeneration.manageIdAndSendEmail();
    // sendInterviewLinkToTheUsers();
    // }
    // }
    // } catch (Exception e) {
    // // added
    // }

    // }

    // immediate mail
    // @Scheduled(cron = "*/10 * * * * *")
    // public void sendInterviewLinkForTheUser() {
    // try {

    // List<InterviewEntity> dataAvaliable = interviewRepository
    // .findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentFalse();
    // System.out.println("syed yasar");
    // // System.out.println(dataAvaliable);
    // System.out.println("syed yasar");
    // for (InterviewEntity interview1 : dataAvaliable) {
    // if (interview1.isScheduled() && interview1.isSchedulingEmail()
    // && !interview1.isInterviewLinkEmailSent()) {
    // System.out.println("im here please tell my name");
    // interview1.setInterviewLinkEmailSent(true);
    // System.out.println("i have changed the status");
    // // System.out.println((ChronoUnit.HOURS.between(interview1.getDate(),
    // // LocalDateTime.now())));
    // String meetingId = interviewLinkGeneration.createMeeting(interview1).block();
    // if (meetingId != null) {
    // System.out.println("im inside the cron still ");
    // interviewRepository.save(interview1);
    // System.out.println("im inside the cron and i have saved the data");
    // } else {
    // System.out.println("the meeting id is not yet generated");
    // }
    // interviewLinkGeneration.manageIdAndSendEmail();
    // sendInterviewLinkToTheUsers();
    // }
    // }
    // } catch (Exception e) {
    // // added
    // }

    // }

    // Zone
    @Scheduled(cron = "*/5 * * * * *") // Run the cron job every minute
    public void sendInterviewLinkForTheUser() {
        try {
            // Parse the application-specific timezone
            ZoneId zoneId = ZoneId.of(appSpecificTimeZone);

            // Get current time in the specified timezone and truncate to minutes
            ZonedDateTime now = ZonedDateTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            System.out.println("Current Time (formatted): " + now.format(formatter));

            // Fetch interviews scheduled but without interview link email sent
            List<InterviewEntity> upcomingInterviews = interviewRepository
                    .findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentFalse();

            for (InterviewEntity interview : upcomingInterviews) {
                // Convert interview date to the specified timezone and truncate for consistent
                // comparison
                ZonedDateTime interviewTime = interview.getDate().atZone(zoneId).truncatedTo(ChronoUnit.MINUTES);
                System.out.println("Interview Scheduled Time (formatted): " +
                        interviewTime.format(formatter));

                // Calculate the difference in minutes
                long differenceInMinutes = ChronoUnit.MINUTES.between(now, interviewTime);
                System.out.println("Difference in Minutes: " + differenceInMinutes);

                // Check if the interview is scheduled within the next 60 minutes
                // if (!differenceInMinutes) {
                System.out.println("Preparing to send interview link for: " +
                        interview.getCandidateName());

                if (interview.isScheduled() && interview.isSchedulingEmail()
                        && !interview.isInterviewLinkEmailSent()) {

                    String meetingId = interviewLinkGeneration.createMeeting(interview).block();
                    if (meetingId != null) {
                        System.out.println("Meeting ID generated for: " +
                                interview.getCandidateName());

                        // Update interview entity and send the email
                        interview.setMeetingId(meetingId);
                        interview.setInterviewLinkEmailSent(true);
                        interviewRepository.save(interview);

                        interviewLinkGeneration.manageIdAndSendEmail();
                        sendInterviewLinkToTheUsers();

                        System.out.println("Interview link email sent for: " +
                                interview.getCandidateName());
                    } else {
                        System.out.println("Failed to generate meeting link for: " +
                                interview.getCandidateName());
                    }
                }
                // }
                else {
                    System.out.println("Interview not within 1-hour window for: " +
                            interview.getCandidateName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred while sending interview links:");
            e.printStackTrace();
        }
    }

    public void sendInterviewLinkToTheUsers() {
        List<InterviewEntity> data = interviewRepository.findByIsTokenGeneratedTrue();
        System.out.println("This is the data" + data);

        for (InterviewEntity userdetails : data) {
            System.out.println("in here in the method to send the inter view link");
            try {
                String emailContent = String.format(
                        "<html>\r\n" +
                                "<body>\r\n" +
                                "<div style=\"background-color:#e0e0e0; padding:10px 100px 30px 100px; text-align:center; font-family:Georgia,serif; margin:auto; width:500px;\">\r\n"
                                +
                                "  <div style=\"color:blue; font-size:25px; padding-bottom:10px; font-family:Georgia,serif;\">Parseez</div>\r\n"
                                +
                                "  <div style=\"background-color:#fff; padding:15px; text-align:left;\">\r\n" +
                                "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:20px;\">Dear Candidate,</p>"
                                +
                                "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px;\">Please find login credentials to join interview</p>\r\n"
                                +
                                "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px;\">Link: &nbsp;"
                                +
                                "      <a href=\"https://aippoint.ai/aippoint-userinterface/coding_login\" style=\"font-size:14px; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6; margin-bottom:0px; color:blue;\">https://aippoint.ai/aippoint-userinterface/coding_login</a>"
                                +
                                "    </p>\r\n" +
                                "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6;\">Password: <span style=\"color:blue;\">%s</span></p>\r\n"
                                +
                                "    <p style=\"font-size:14px; color:#000000; margin:0; text-align:left; font-family:Georgia,serif; line-height:1.6;\"><br/><br/>Thank you,<br/>Team Parseez</p>\r\n"
                                +
                                "  </div>\r\n" +
                                "</div>\r\n" +
                                "</body>\r\n" +
                                "</html>\r\n",
                        userdetails.getPassword());
                MailService.sendEmail(userdetails.getEmail(),
                        " [Parseez] Please find login credentials to join interview",
                        emailContent);
                userdetails.setTokenGenerated(false);
                interviewRepository.save(userdetails);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }

    }

}
