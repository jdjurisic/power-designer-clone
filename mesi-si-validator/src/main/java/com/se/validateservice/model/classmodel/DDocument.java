package com.se.validateservice.model.classmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DDocument {

    private String id;

    private String linkKeyProperty;
    private String linkFromPortIdProperty;
    private String linkToPortIdProperty;
    private ModelData modelData;

    private List<NodeData> nodeDataArray;

    private List<LinkData> linkDataArray;
}

