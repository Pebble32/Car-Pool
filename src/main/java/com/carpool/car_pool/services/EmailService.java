package com.carpool.car_pool.services;

import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static com.carpool.car_pool.services.EmailTemplate.RIDE_REQUEST;

/**
 * Service for emails to users.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * Sends a simple email with no HTML.
     *
     * @param to      The recipient's email address.
     * @param subject The email's subject.
     * @param text    The email's body.
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
