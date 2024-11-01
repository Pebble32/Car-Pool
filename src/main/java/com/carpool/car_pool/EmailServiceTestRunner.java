package com.carpool.car_pool;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class EmailServiceTestRunner implements CommandLineRunner {

    private final EmailService emailService;

    public EmailServiceTestRunner(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {

        String to = "sigurthor.maggi@gmail.com";
        String name = "S. Maggi Snorrason";
        String subject = "Test Email Subject";
        String text = "This is a test email body.";

        emailService.sendSimpleEmail(to, subject, text);

        System.out.println("Simple test email sent!");

        UserEntity receiver = new UserEntity();
        UserEntity requester = new UserEntity();
        RideOfferEntity rideOffer = new RideOfferEntity();
        RideRequestsEntity rideRequest = new RideRequestsEntity();

        receiver.setFirstname("Maggi");
        receiver.setEmail("sigurthor.maggi@gmail.com");

        requester.setFirstname("Lilja");
        requester.setLastname("Schopka");

        rideOffer.setStartLocation("Breiðholt");
        rideOffer.setEndLocation("Háskóli Íslands");
        rideOffer.setDepartureTime(LocalDateTime.now());

        rideRequest.setRequester(requester);
        rideRequest.setRideOffer(rideOffer);

        try {
            emailService.sendRideRequestNotificationEmail(receiver, rideRequest);

            System.out.println("HTML test email sent!");
        } catch (MessagingException e) {
            System.out.println("Sending HTML email failed!");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
