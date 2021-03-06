package ru.kpfu.itis.issst.search.controller;

import org.apache.uima.UIMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;
import ru.kpfu.itis.issst.search.dto.AnnotatedDocument;
import ru.kpfu.itis.issst.search.service.DocumentStorage;
import ru.kpfu.itis.issst.search.service.UIMAService;
import ru.kpfu.itis.issst.search.service.UIDGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Nikita
 * since: 05.05.2014
 */
@Controller
public class UIMADemoController extends BaseController {
    @Autowired
    private UIMAService uimaService;

    /**
     * @param text source text
     * @return some dirty info about all annotations, like their span's
     * @throws IOException
     * @throws UIMAException
     */
    @RequestMapping(value = "/getDirtyAnnotations", method = {RequestMethod.GET, RequestMethod.POST})
    public String getAllAnnotation(@RequestParam String text) throws IOException, UIMAException {
        String annotationInfo = uimaService.getAllAnnotationsAsString(text);

        request.setAttribute("initialText", text);
        request.setAttribute("annotations", annotationInfo.replace("\n", "<br>"));

        return "minimalUimaTemplate";
    }

    /**
     * @param text source text
     * @return the same that XMI consumer returns
     * @throws UIMAException
     * @throws SAXException
     * @throws IOException
     */
    @RequestMapping(value = "/getXmi", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpEntity<byte[]> getJCasXmiWay(@RequestParam String text)
            throws UIMAException, SAXException, IOException {
        String xml = uimaService.getXmlTranslatedResult(text);

        // Dirty way to return xml without marshalling view
        byte[] documentBody = xml.getBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }
}
