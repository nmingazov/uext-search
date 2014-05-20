package ru.kpfu.itis.issst.search.controller;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;
import ru.kpfu.itis.issst.search.dto.annotation.Position;
import ru.kpfu.itis.issst.search.dto.annotation.SolrSentence;
import ru.kpfu.itis.issst.search.service.DocumentStorage;
import ru.kpfu.itis.issst.search.service.SearchService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: Nikita
 * since: 20.05.2014
 */
@Controller
public class SearchController extends BaseController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private DocumentStorage documentStorage;

    @RequestMapping(value = "/searchByQuery", method = RequestMethod.GET)
    public String searchSentencesByQuery(@RequestParam String query) throws SolrServerException {
        List<SolrSentence> annotations = searchService.searchByQuery(query, SolrSentence.class);

        Set<String> documentIds = new HashSet<String>();
        for (SolrSentence annotation: annotations) {
            for (Position position: annotation.getPositions()) {
                documentIds.add(position.getDocumentId());
            }
        }
        Map<String, AnnotatedDocument> annotatedDocumentMap = documentStorage.findAllByDocumentOnPositions(documentIds);

        request.setAttribute("annotationsList", annotations);
        request.setAttribute("annotatedDocumentMap", annotatedDocumentMap);

        return "searchResult";
    }
}
