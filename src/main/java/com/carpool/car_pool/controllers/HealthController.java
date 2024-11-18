package com.carpool.car_pool.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is operational!");
    }
}

