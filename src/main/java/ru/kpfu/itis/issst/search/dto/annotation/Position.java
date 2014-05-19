package ru.kpfu.itis.issst.search.dto.annotation;

/**
 * author: Nikita
 * since: 19.05.2014
 */
public class Position {
    private String documentId;
    private int begin;
    private int end;

    public Position(String documentId, int begin, int end) {
        this.documentId = documentId;
        this.begin = begin;
        this.end = end;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
