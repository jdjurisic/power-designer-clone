package com.se.ucservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeData {

    private String key;
    private String name;

    private List<Property> properties;
    private List<Method> methods;

    private String category;
    private String loc;
}
