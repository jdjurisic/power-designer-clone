package com.se.ucservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeData
{
    private String key;
    private String text;
    private String color;
    private String category;
    private String loc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return Objects.equals(text, nodeData.text) && Objects.equals(color, nodeData.color) && Objects.equals(category, nodeData.category);
    }
}

//    key:string;
//            text:string;
//            color:string;
//            category:string;
//            loc:string;
