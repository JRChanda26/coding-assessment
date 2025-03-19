package com.interview.interview_service.commonservice;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.SubjectTerm;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("notify@parseez.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        // helper.addInline("emailimage", new ClassPathResource("emailopen.jpeg"));
        javaMailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, MultipartFile multipartFile)
            throws MessagingException, IllegalStateException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("notify@parseez.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        File file = convertMultipartFileToFile(multipartFile);

        helper.addAttachment(multipartFile.getOriginalFilename(), file);

        javaMailSender.send(message);
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String prefix = "temp";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        return file;
    }

    public void readEmails() throws IOException {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "jenishmerin4@gmail.com", "fdonmjiqljcobgcm");
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            // Search for messages with the given subject
            Message[] messages = inbox.search(new SubjectTerm("hiring"), inbox.getMessages());
            // Get the latest 10 matching messages
            int messageCount = messages.length;
            int startIndex = Math.max(0, messageCount - 10);
            int endIndex = Math.max(0, messageCount - 1);
            Message[] latestMessages = new Message[endIndex - startIndex + 1];
            System.arraycopy(messages, startIndex, latestMessages, 0, endIndex - startIndex + 1);
            for (Message message : latestMessages) {
                // Process the email message
                System.out.println("Subject: " + message.getSubject());
            }
            inbox.close(false);
            store.close();
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }
}