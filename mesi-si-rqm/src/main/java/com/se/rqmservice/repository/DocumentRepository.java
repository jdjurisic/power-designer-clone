package com.se.rqmservice.repository;

import com.se.rqmservice.model.DDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DDocument, String> {
    Long deleteDocumentById(String id);
}
