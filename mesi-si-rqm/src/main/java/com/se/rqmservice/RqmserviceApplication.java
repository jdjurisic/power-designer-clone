package com.se.rqmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.se.rqmservice.*"})
public class RqmserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqmserviceApplication.class, args);
    }

}
