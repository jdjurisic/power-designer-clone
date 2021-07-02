package com.se.cmservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.util.List;

@Document(collection = "classmodels")
@Data
public class DDocument {

    @Id
    private String id;

    private String linkKeyProperty;
    private String linkFromPortIdProperty;
    private String linkToPortIdProperty;
    private ModelData modelData;

    private List<NodeData> nodeDataArray;

    private List<LinkData> linkDataArray;


    private String previousVersionId;
    private String nextVersionId;

    private String lastModifiedBy;

}
