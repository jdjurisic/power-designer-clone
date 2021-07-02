package com.se.cmservice.servicemodel;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokerParameter {


    public BrokerParameter(String name, String type){
        this.name = name;
        this.type = type;
    }

    private String name;

    private String type;

    private ParameterType parameterType;


}
