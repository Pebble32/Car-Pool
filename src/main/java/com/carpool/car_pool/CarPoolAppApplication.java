package com.carpool.car_pool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CarPoolAppApplication {
    // dkm2@hi.is
    // lks17hi.is
    // sms70@hi.is
    public static void main(String[] args) {
        SpringApplication.run(CarPoolAppApplication.class, args);
    }

}
