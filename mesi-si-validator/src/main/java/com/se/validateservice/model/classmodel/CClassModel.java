package com.se.validateservice.model.classmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CClassModel {

    private int id;
    @NotNull
    private String name;
    private String color;
    private List<Property> properties;
    private List<Method> methods;

}

