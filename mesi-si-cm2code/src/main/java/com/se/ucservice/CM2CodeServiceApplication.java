package com.se.ucservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.se.ucservice.*"})
public class CM2CodeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CM2CodeServiceApplication.class, args);
    }
}
