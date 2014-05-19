package ru.kpfu.itis.issst.search.service;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.issst.search.dto.annotation.BaseAnnotation;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;


/**
 * author: Nikita
 * since: 19.05.2014
 */
@Service
public class SearchService {
    @Value("${solr.url}")
    private String solrServerUrl;

    /**
     * according to http://wiki.apache.org/solr/Solrj, HttpSolrServer is thread-safe
     * moreover, SearchService is a singleton
     */
    private SolrServer solrServer = new HttpSolrServer(solrServerUrl);


    @PostConstruct
    private void initialize() {
        solrServer = new HttpSolrServer(solrServerUrl);
    }

    public void postAnnotations(List<BaseAnnotation> annotations) throws IOException, SolrServerException {
        solrServer.addBeans(annotations);
        solrServer.commit();
    }
}
