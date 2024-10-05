package com.needibay.cart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.recipients}")
    private String[] recipients;

    @Value("${email.subject.loggingFilter}")
    private String loggingFilterSubject;

    @Value("${email.subject.globalExceptionHandler}")
    private String globalExceptionHandlerSubject;

    public void sendErrorEmail(String subject, Exception ex) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipients);
            message.setSubject(subject);
            message.setText("An error occurred: " + ex.getMessage());
            mailSender.send(message);
            logger.info("Error email sent successfully to: {}", (Object) recipients);
        } catch (Exception emailEx) {
            logger.error("Failed to send error email: {}", emailEx.getMessage(), emailEx);
        }
    }
}
