package com.se.validateservice.model.classmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {

    private String name;
    private String type;
    private List<Parameter> parameters;
    private String visibility;
}
