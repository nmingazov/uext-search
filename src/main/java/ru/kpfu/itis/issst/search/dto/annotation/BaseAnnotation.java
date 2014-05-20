package ru.kpfu.itis.issst.search.dto.annotation;

import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Nikita
 * since: 19.05.2014
 */
public class BaseAnnotation {
    @Field
    protected String span;

    @Field
    protected List<String> documentPositions;

    /**
     * used inside solrj!
     */
    public BaseAnnotation() {}

    public BaseAnnotation(String span, Position position) {
        this.span = span;
        this.addPosition(position);
    }

    protected String createDocumentIdWithPosition(Position position) {
        return position.getDocumentId() + ":" + position.getBegin() + ":" + position.getEnd();
    }

    protected Position parseDocumentIdWithPosition(String documentIdWithPosition) {
        if (!documentIdWithPosition.contains(":")) throw new RuntimeException("documentPositions doesn't contains ':'!");
        String[] tmp = documentIdWithPosition.split(":");
        if (tmp.length != 3) throw new RuntimeException("documentPositions has wrong format!");
        String id = tmp[0];
        int begin;
        int end;
        try {
            begin = Integer.parseInt(tmp[1]);
            end = Integer.parseInt(tmp[2]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("documentPositions's begin and end can't be parsed!");
        }
        return new Position(id, begin, end);
    }

    public void addPosition(Position position) {
        if (this.documentPositions == null) {
            documentPositions = new ArrayList<String>();
        }
        documentPositions.add(createDocumentIdWithPosition(position));
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public List<Position> getPositions() {
        List<Position> result;
        if (this.documentPositions == null || this.documentPositions.isEmpty()) {
            result =  new ArrayList<Position>(0);
        } else {
            result = new ArrayList<Position>(documentPositions.size());
            for (String unparsed: documentPositions) {
                result.add(parseDocumentIdWithPosition(unparsed));
            }
        }
        return result;
    }

    /**
     * used only in solrj
     */
    public List<String> getDocumentPositions() {
        return documentPositions;
    }

    /**
     * used only in solrj
     */
    public void setDocumentPositions(List<String> documentPositions) {
        this.documentPositions = documentPositions;
    }
}
