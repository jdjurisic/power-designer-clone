package com.se.ucservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.se.ucservice.*"})
public class UseCaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseCaseServiceApplication.class, args);
    }

}
