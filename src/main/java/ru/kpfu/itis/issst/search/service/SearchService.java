package ru.kpfu.itis.issst.search.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
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

    public void postAnnotations(List<? extends BaseAnnotation> annotations) throws IOException, SolrServerException {
        solrServer.addBeans(annotations);
        solrServer.commit();
    }

    public <T extends BaseAnnotation> List<T> searchByQuery(String query, Class<T> type) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        QueryResponse rsp = solrServer.query(solrQuery);
        return rsp.getBeans(type);
    }
}