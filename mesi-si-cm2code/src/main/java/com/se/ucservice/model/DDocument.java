package com.se.ucservice.model;

import lombok.Data;

import java.util.List;

@Data
public class DDocument {

    private String id;

    private String linkKeyProperty;
    private String linkFromPortIdProperty;
    private String linkToPortIdProperty;
    private ModelData modelData;

    private List<NodeData> nodeDataArray;

    private List<LinkData> linkDataArray;
}

