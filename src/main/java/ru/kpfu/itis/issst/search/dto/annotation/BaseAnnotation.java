package ru.kpfu.itis.issst.search.dto.annotation;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * author: Nikita
 * since: 19.05.2014
 */
public class BaseAnnotation {
    @Field("span")
    private String span;

    @Field("documentIdWithPosition")
    private List<String> documentIdWithPosition;

    /**
     * used inside solrj!
     */
    public BaseAnnotation() {}

    public BaseAnnotation() {

    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getDocumentIdWithPosition() {
        return documentIdWithPosition;
    }

    public void setDocumentIdWithPosition(String documentIdWithPosition) {
        this.documentIdWithPosition = documentIdWithPosition;
    }
}
