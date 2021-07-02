package com.se.cmservice.repository;

import com.se.cmservice.model.DDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DDocument, String> {
    Long deleteDocumentById(String id);
}
