package com.se.validateservice.model.broker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    public Parameter(String name, String type){
        this.name = name;
        this.type = type;
    }

    private String name;

    private String type;

    private ParameterType parameterType;

}
