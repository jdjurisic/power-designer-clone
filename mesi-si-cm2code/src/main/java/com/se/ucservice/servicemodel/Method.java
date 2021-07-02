package com.se.ucservice.servicemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {

    private String name;

    private String url;

    private HttpMethod httpMethod;

    private List<Parameter> request;

    private List<Parameter> response;

    private List<UserType> allowedTypes;

    private SService service;
}
