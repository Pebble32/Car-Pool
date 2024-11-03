package com.carpool.car_pool.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(String userEmail, String notification) {
        // Send to the user-specific queue
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", notification);
    }
}
