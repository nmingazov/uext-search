package ru.kpfu.itis.issst.search.dto;

import org.springframework.data.annotation.Id;

/**
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

    public AnnotatedDocument(String id, String plainText, String xmiView) {
        this.id = id;
        this.plainText = plainText;
        this.xmiView = xmiView;
        if (plainText != null) {
            if (plainText.length() <= FIRST_SYMBOLS_AMOUNT) {
                this.firstSymbols = plainText;
            } else {
                this.firstSymbols = plainText.substring(0, FIRST_SYMBOLS_AMOUNT);
            }
        }
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

    @Override
    public String toString() {
        return "AnnotatedDocument{" +
                "id='" + id + '\'' +
                ", plainText='" + plainText + '\'' +
                ", xmiView='" + xmiView + '\'' +
                ", firstSymbols='" + firstSymbols + '\'' +
                '}';
    }
}
