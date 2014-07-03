package ru.kpfu.itis.issst.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.issst.search.dto.annotation.SolrSentence;

import java.util.List;

/**
 * author: Nikita
 * since: 03.07.2014
 */
@Service
public class AsyncProcessor {
    @Autowired
    private UIMAService uimaService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private DocumentStorage storage;

    @Async
    public void asyncDocumentProcessing(String uid, String text) throws Exception {
        // firstly, preprocessing
        String xmi = uimaService.getXmlTranslatedResult(text);

        // Thread.sleep(10000); uncomment to test completeness async, todo remove
        // secondly, update document into base
        storage.updateDocumentXMI(uid, xmi);

        // then, run indexing
        asyncDocumentIndexing(uid, text);
    }

    @Async
    public void asyncDocumentIndexing(String uid, String text) throws Exception {
        // firstly, get all needed annotations
        // todo: should do it from XMI
        List<SolrSentence> solrSentences = uimaService.getSentenceAnnotations(uid, text);

        // Thread.sleep(10000); uncomment to test completeness async, todo remove
        // secondly, post annotations to solr server
        searchService.postAnnotations(solrSentences);

        // then, set status
        storage.updateIsIndexedFlag(uid, true);
    }
}
