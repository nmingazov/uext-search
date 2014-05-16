package ru.kpfu.itis.issst.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;

/**
 * author: Nikita
 * since: 15.05.2014
 */
@Service
public class DocumentStorage {
    @Value("${mongo.dbname}")
    private String DOCUMENT_DATABASE;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void add(AnnotatedDocument document) {
        mongoTemplate.save(document, DOCUMENT_DATABASE);
    }

    public AnnotatedDocument getById(String id) {
        return mongoTemplate.findById(id, AnnotatedDocument.class, DOCUMENT_DATABASE);
    }
}
