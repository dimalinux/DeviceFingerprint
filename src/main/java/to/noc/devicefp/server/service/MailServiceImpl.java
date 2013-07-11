/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import to.noc.devicefp.server.domain.entity.Feedback;
import to.noc.devicefp.server.domain.entity.OpenIdUser;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private MailSender mailSender;

    @Value("${mail.feedback.address}")
    private String feedbackEmailAddress;

    @Override
    public boolean sendMail(
            String cc,
            String subject,
            String messageBody) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(feedbackEmailAddress);
            message.setFrom(feedbackEmailAddress);

            if (cc != null) {
                message.setCc(cc);
            }
            if (subject != null) {
                message.setSubject(subject);
            }
            if (messageBody != null) {
                message.setText(messageBody);
            }

            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Sending email failed", ex);
            return false;
        }
        return true;
    }

    @Async
    @Override
    public void sendFeedbackAsync(Feedback feedback) {
        String cc = feedback.getUser().getEmail();
        String subject = "Device Info Feedback from " + feedback.getUser().getFullName();
        String messageBody = feedback.getMessage();
        sendMail(cc, subject, messageBody);
    }

    @Async
    @Override
    public void sendNotificationOfNewUserAsync(OpenIdUser oidUser) {
        String fullName = oidUser.getFullName();

        String subject = "New site user: ";
        if (fullName != null) {
            subject += fullName;
        }

        StringBuilder messageBody = new StringBuilder("New site user created:\n");
        messageBody.append("Fullname: ").append(fullName).append("\n");
        messageBody.append("Email: ").append(oidUser.getEmail()).append("\n");
        messageBody.append("Request Host: ").append(oidUser.getRequestHost()).append("\n");
        sendMail(null, subject, messageBody.toString());
    }

}