package com.se.cmservice.servicemodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se.cmservice.model.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SService {


    private String name;

    private int port;

    private String url;

    @JsonIgnoreProperties("service")
    private List<BrokerMethod> methods;



}
