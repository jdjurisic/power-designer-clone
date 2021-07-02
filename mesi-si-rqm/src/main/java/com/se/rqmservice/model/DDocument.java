package com.se.rqmservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import java.util.List;

@Document(collection = "rqms")
@Data
public class DDocument {

    @Id
    private String id;

    @Valid
    private List<Rqm> rqms;


    private String previousVersionId;
    private String nextVersionId;

    private String lastModifiedBy;

    private List<String> generatedUseCaseIDs;
}
