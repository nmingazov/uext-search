package ru.kpfu.itis.issst.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;

import java.util.List;

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

    public boolean exists(String id) {
        return mongoTemplate.exists(new Query(Criteria.where("id").is(id)), AnnotatedDocument.class, DOCUMENT_DATABASE);
    }

    public void delete(String id) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)), AnnotatedDocument.class, DOCUMENT_DATABASE);
    }

    public List<AnnotatedDocument> findAll(int offset, int limit) {
        Query query = new Query();
        query.skip(offset).limit(limit);
        return mongoTemplate.find(query, AnnotatedDocument.class, DOCUMENT_DATABASE);
    }
}
