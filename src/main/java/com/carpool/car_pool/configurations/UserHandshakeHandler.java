package com.carpool.car_pool.configurations;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // Get the HTTP session
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

        if (httpSession == null) {
            return null; // Reject if no session
        }

        // Get userEmail from the session
        String userEmail = (String) httpSession.getAttribute("userEmail");

        if (userEmail == null) {
            return null; // Reject if not authenticated
        }

        // Set the userEmail as the Principal's name
        return new Principal() {
            @Override
            public String getName() {
                return userEmail;
            }
        };
    }
}
