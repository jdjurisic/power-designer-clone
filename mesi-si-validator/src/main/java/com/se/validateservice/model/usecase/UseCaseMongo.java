package com.se.validateservice.model.usecase;

import lombok.Data;

import java.util.List;

@Data
public class UseCaseMongo {

    private String linkKeyProperty = "key";
    private String linkFromPortIdProperty = "fromPort";
    private String linkToPortIdProperty = "toPort";
    private UseCaseModelData useCaseModelData;
    private List<UseCaseNodeData> nodeDataArray;
    private List<UseCaseLinkData> linkDataArray;
}
