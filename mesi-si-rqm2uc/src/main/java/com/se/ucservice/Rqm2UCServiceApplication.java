package com.se.ucservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.se.ucservice.*"})
public class Rqm2UCServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Rqm2UCServiceApplication.class, args);
    }

}
