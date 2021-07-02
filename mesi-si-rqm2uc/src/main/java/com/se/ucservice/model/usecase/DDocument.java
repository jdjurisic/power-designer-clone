package com.se.ucservice.model.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DDocument {


    private String linkKeyProperty;
    private String linkFromPortIdProperty;
    private String linkToPortIdProperty;
    private ModelData modelData;
    private List<NodeData> nodeDataArray;
    private List<LinkData> linkDataArray;
}
