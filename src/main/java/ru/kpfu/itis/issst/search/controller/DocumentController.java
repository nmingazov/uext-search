package ru.kpfu.itis.issst.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;
import ru.kpfu.itis.issst.search.dto.annotation.SolrSentence;
import ru.kpfu.itis.issst.search.service.DocumentStorage;
import ru.kpfu.itis.issst.search.service.SearchService;
import ru.kpfu.itis.issst.search.service.UIDGenerator;
import ru.kpfu.itis.issst.search.service.UIMAService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * author: Nikita
 * since: 16.05.2014
 */
@Controller
public class DocumentController extends BaseController {
    public static final Integer DEFAULT_OFFSET = 0;
    public static final Integer DEFAULT_LIMIT = 10;

    @Autowired
    private UIMAService uimaService;

    @Autowired
    private UIDGenerator uidGenerator;

    @Autowired
    private DocumentStorage storage;

    @Autowired
    private SearchService searchService;

    @Async
    private void asyncDocumentProcessing(String uid, String text) throws Exception {
        String xmi = uimaService.getXmlTranslatedResult(text);
        // todo: update document xmi in database

        // run indexing
        asyncDocumentIndexing(uid, text);
    }

    @Async
    private void asyncDocumentIndexing(String uid, String text) throws Exception {
        List<SolrSentence> solrSentences = uimaService.getSentenceAnnotations(uid, text);
        searchService.postAnnotations(solrSentences);

        // todo: update document indexing flag
    }

    @RequestMapping(value = "/postDocument", method = {RequestMethod.GET, RequestMethod.POST})
    public String postDocumentToDatabase(@RequestParam String text, HttpServletResponse response) throws Exception {
        if (text.isEmpty()) return badRequest(response);

        // firstly, generate id
        String uid = uidGenerator.getUID();

        // secondly, add to a storage
        // can't be async, because we need to be sure that document is in the database
        storage.add(new AnnotatedDocument(uid, text));

        // thirdly, run preprocessing and indexing
        asyncDocumentProcessing(uid, text);

        // then, return docID to user
        request.setAttribute("documentId", uid);
        response.setStatus(HttpStatus.CREATED.value());
        return "documentSaved";
    }

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.GET)
    public String getAllDocuments(
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit,
        HttpServletResponse response) throws IOException {

        // dodging requests like ?offset=
        if (offset == null) offset = DEFAULT_OFFSET;
        if (limit == null) limit = DEFAULT_LIMIT;

        List<AnnotatedDocument> documents = storage.findAllIdsWithDescription(offset, limit);
        if (documents.isEmpty()) return notFound(response);

        long count = storage.getCount();

        request.setAttribute("documents", documents);
        request.setAttribute("count", count);
        return "allDocuments";
    }

    @RequestMapping(value = "/getDocumentPlainText", method = RequestMethod.GET)
    public String getDocumentAsAPlainText(@RequestParam String id, HttpServletResponse response) throws Exception {
        if (id.isEmpty()) return badRequest(response);

        AnnotatedDocument document = storage.getById(id);
        if (document == null) return notFound(response);

        request.setAttribute("document", document);
        return "documentAsAPlainText";
    }

    @RequestMapping(value = "/getDocumentXMI", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDocumentAsXMI(@RequestParam String id, HttpServletResponse response) throws Exception {
        // todo: find a clean way to return xml
        if (id.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value());
            return null;
        }

        AnnotatedDocument document = storage.getById(id);
        if (document == null) {
            response.sendError(HttpStatus.BAD_REQUEST.value());
            return null;
        }

        byte[] documentBody = document.getXmiView().getBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }

    @RequestMapping(value = "/deleteDocumentById", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteDocumentById(@RequestParam String id, HttpServletResponse response) throws Exception {
        if (id.isEmpty()) return badRequest(response);

        if (storage.exists(id)) {
            storage.delete(id);
        } else {
            return notFound(response);
        }

        request.setAttribute("documentId", id);
        return "documentDeleted";
    }
}