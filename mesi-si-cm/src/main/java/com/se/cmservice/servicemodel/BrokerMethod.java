package com.se.cmservice.servicemodel;

import org.springframework.http.HttpMethod;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMethod {


    private String name;

    private String url;

    private HttpMethod httpMethod;

    private List<BrokerParameter> request;

    private List<BrokerParameter> response;

    private List<UserType> allowedTypes;

    private SService service;

}
