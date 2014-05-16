package ru.kpfu.itis.issst.search.dto;

import org.springframework.data.annotation.Id;

/**
 * author: Nikita
 * since: 13.05.2014
 */
public class AnnotatedDocument {
    @Id
    private String id;

    /**
     * Source text of document
     */
    private String plainText;

    /**
     * Translated to XMI
     */
    private String xmiView;

    public AnnotatedDocument(){}

    public AnnotatedDocument(String id, String plainText, String xmiView) {
        this.id = id;
        this.plainText = plainText;
        this.xmiView = xmiView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getXmiView() {
        return xmiView;
    }

    public void setXmiView(String xmiView) {
        this.xmiView = xmiView;
    }

    @Override
    public String toString() {
        return "AnnotatedDocument{" +
                "id='" + id + '\'' +
                ", plainText='" + plainText + '\'' +
                ", xmiView='" + xmiView + '\'' +
                '}';
    }
}
