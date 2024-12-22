package com.example.tubesPBW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class tubesPBWApplication {

    public static void main(String[] args) {
        SpringApplication.run(tubesPBWApplication.class, args);
    }

}
