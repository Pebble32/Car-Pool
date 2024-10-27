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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender mailSender;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    public void sendRideRequestNotificationEmail(UserEntity receiver, RideRequestsEntity rideRequest) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", receiver.getFirstname());
        context.setVariable("requestersFullName", rideRequest.getRequester().getFullName());
        context.setVariable("requestersFirstname", rideRequest.getRequester().getFirstname());
        context.setVariable("startLocation", rideRequest.getRideOffer().getStartLocation());
        context.setVariable("endLocation", rideRequest.getRideOffer().getEndLocation());
        context.setVariable("departureTime", rideRequest.getRideOffer().getDepartureTime().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        ));
        context.setVariable("requestLink", "https://www.mbl.is/frettir/"); // TODO: Replace with the correct link.

        String subject = rideRequest.getRequester().getFirstname()
                + " asked for a ride to "
                + rideRequest.getRideOffer().getEndLocation();

        sendHtmlEmail(receiver.getEmail(), subject, context, RIDE_REQUEST);
    }

    /**
     * Sends an HTML email.
     *
     * @param to            The recipient's email address.
     * @param context       The variables for the email.
     * @param emailTemplate The email HTML template to use.
     * @throws MessagingException The base class for all exceptions thrown by the Messaging classes.
     */
    private void sendHtmlEmail(String to, String subject, Context context, EmailTemplate emailTemplate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String htmlContent = templateEngine.process(emailTemplate.getHtml(), context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
