package com.se.validateservice.model.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUseCaseModel {

    private String id;

    private String linkKeyProperty;
    private String linkFromPortIdProperty;
    private String linkToPortIdProperty;
    private UseCaseModelData modelData;
    private List<UseCaseNodeData> nodeDataArray;
    private List<UseCaseLinkData> linkDataArray;


}
