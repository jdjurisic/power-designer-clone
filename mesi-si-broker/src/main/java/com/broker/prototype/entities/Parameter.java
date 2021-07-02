package com.broker.prototype.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class Parameter {


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Enumerated
    private ParameterType parameterType;



}
