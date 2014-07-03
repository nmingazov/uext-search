package ru.kpfu.itis.issst.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;

import java.util.*;

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

    /**
     * updates xmi for given id, also set "isAnnotated" to true
     * @param id documentId
     * @param xmi UIMA XMI view
     */
    public void updateDocumentXMI(String id, String xmi) {
        mongoTemplate.findAndModify(
                new Query(Criteria.where("id").is(id)),
                new Update().set("xmiView", xmi).set("isAnnotated", true),
                AnnotatedDocument.class,
                DOCUMENT_DATABASE
        );
    }

    /**
     * sets flag isIndexed to document with given id
     * @param id documentId
     * @param isIndexed UIMA XMI view
     */
    public void updateIsIndexedFlag(String id, boolean isIndexed) {
        mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(id)),
                Update.update("isIndexed", isIndexed),
                AnnotatedDocument.class,
                DOCUMENT_DATABASE
        );
    }

    /**
     * returns only _id and firstSymbols!
     * @param offset
     * @param limit
     * @return
     */
    public List<AnnotatedDocument> findAllIdsWithDescription(int offset, int limit) {
        Query query = new Query();
        query.skip(offset).limit(limit);
        query.fields().include("_id").include("firstSymbols");
        return mongoTemplate.find(query, AnnotatedDocument.class, DOCUMENT_DATABASE);
    }

    /**
     * returns only _id and firstSymbols!
     * @return
     */
    public Map<String, AnnotatedDocument> findAllByDocumentOnPositions(Collection<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        query.fields().include("_id").include("firstSymbols");

        List<AnnotatedDocument> documents = mongoTemplate.find(query, AnnotatedDocument.class, DOCUMENT_DATABASE);
        Map<String, AnnotatedDocument> result = new HashMap<String, AnnotatedDocument>();
        for (AnnotatedDocument document: documents) {
            result.put(document.getId(), document);
        }
        return result;
    }

    public long getCount() {
        return mongoTemplate.count(new Query(), DOCUMENT_DATABASE);
    }
}
