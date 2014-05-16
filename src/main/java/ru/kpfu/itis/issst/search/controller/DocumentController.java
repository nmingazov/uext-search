package ru.kpfu.itis.issst.search.controller;

import org.apache.uima.UIMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;
import ru.kpfu.itis.issst.search.service.DocumentStorage;
import ru.kpfu.itis.issst.search.service.UIDGenerator;
import ru.kpfu.itis.issst.search.service.UIMAService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @RequestMapping(value = "/postDocument",method = {RequestMethod.GET, RequestMethod.POST})
    public String postDocumentToDatabase(@RequestParam String text, HttpServletResponse response)
            throws UIMAException, SAXException, IOException {
        if (text.isEmpty()) returnBadRequest(response);

        String uid = uidGenerator.getUID();
        String xmi = uimaService.getXmlTranslatedResult(text);
        storage.add(new AnnotatedDocument(uid, text, xmi));

        request.setAttribute("documentId", uid);
        response.setStatus(HttpStatus.CREATED.value());
        return "documentSaved";
    }
}
