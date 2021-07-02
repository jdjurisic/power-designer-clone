package com.se.ucservice.repository;

import com.se.ucservice.model.DDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DDocument, String> {
    Long deleteDocumentById(String id);
}
