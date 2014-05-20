package ru.kpfu.itis.issst.search.dto.annotation;

/**
 * author: Nikita
 * since: 19.05.2014
 */
public class SolrSentence extends BaseAnnotation {
    public SolrSentence() {super();}

    public SolrSentence(String span, Position position) {
        super(span, position);
    }
}
