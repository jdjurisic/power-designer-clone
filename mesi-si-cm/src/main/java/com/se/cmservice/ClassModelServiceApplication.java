package com.se.cmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.se.cmservice.*"})
public class ClassModelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassModelServiceApplication.class, args);
    }

}
