package com.se.cmservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return Objects.equals(name, nodeData.name) && Objects.equals(properties, nodeData.properties) && Objects.equals(methods, nodeData.methods) && Objects.equals(category, nodeData.category);
    }
}
