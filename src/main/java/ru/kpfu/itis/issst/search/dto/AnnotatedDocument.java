package ru.kpfu.itis.issst.search.dto;

import org.springframework.data.annotation.Id;

/**
 * Class which holds documents
 *
 * todo: all fields hardcoded now, see @DocumentStorage
 * author: Nikita
 * since: 13.05.2014
 */
public class AnnotatedDocument {
    // const for first N symbols stored in DB
    private static final int FIRST_SYMBOLS_AMOUNT = 100;

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

    /**
     * first N symbols
     */
    private String firstSymbols;

    /**
     * Checks if document annotated by UIMA
     */
    private boolean isAnnotated;

    /**
     * Checks if document indexed within Solr
     */
    private boolean isIndexed;

    public AnnotatedDocument() {
    }

    public AnnotatedDocument(String id, String plainText) {
        this.id = id;
        this.plainText = plainText;
        // first symbols
        if (plainText != null) {
            if (plainText.length() <= FIRST_SYMBOLS_AMOUNT) {
                this.firstSymbols = plainText;
            } else {
                this.firstSymbols = plainText.substring(0, FIRST_SYMBOLS_AMOUNT);
            }
        }
        // isAnnotated and isIndexed is 0 by default
    }

    public AnnotatedDocument(String id, String plainText, String xmiView) {
        this(id, plainText);
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

    public String getFirstSymbols() {
        return firstSymbols;
    }

    public boolean isAnnotated() {
        return isAnnotated;
    }

    public void setAnnotated(boolean isAnnotated) {
        this.isAnnotated = isAnnotated;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean isIndexed) {
        this.isIndexed = isIndexed;
    }

    @Override
    public String toString() {
        return "AnnotatedDocument{" +
                "id='" + id + '\'' +
                ", plainText='" + plainText + '\'' +
                ", xmiView='" + xmiView + '\'' +
                ", firstSymbols='" + firstSymbols + '\'' +
                ", isAnnotated=" + isAnnotated +
                ", isIndexed=" + isIndexed +
                '}';
    }
}
