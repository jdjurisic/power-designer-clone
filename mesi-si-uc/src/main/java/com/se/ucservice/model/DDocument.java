package com.se.ucservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.util.List;

@Document(collection = "usecases")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
