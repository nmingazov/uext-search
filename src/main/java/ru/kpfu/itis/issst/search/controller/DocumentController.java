package ru.kpfu.itis.issst.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;
import ru.kpfu.itis.issst.search.service.DocumentStorage;
import ru.kpfu.itis.issst.search.service.UIDGenerator;
import ru.kpfu.itis.issst.search.service.UIMAService;

import javax.servlet.http.HttpServletResponse;

/**
 * author: Nikita
 * since: 16.05.2014
 */
@Controller
public class DocumentController extends BaseController {

    @Autowired
    private UIMAService uimaService;

    @Autowired
    private UIDGenerator uidGenerator;

    @Autowired
    private DocumentStorage storage;

    @RequestMapping(value = "/postDocument", method = {RequestMethod.GET, RequestMethod.POST})
    public String postDocumentToDatabase(@RequestParam String text, HttpServletResponse response) throws Exception {
        if (text.isEmpty()) return badRequest(response);

        String uid = uidGenerator.getUID();
        String xmi = uimaService.getXmlTranslatedResult(text);
        storage.add(new AnnotatedDocument(uid, text, xmi));

        request.setAttribute("documentId", uid);
        response.setStatus(HttpStatus.CREATED.value());
        return "documentSaved";
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
