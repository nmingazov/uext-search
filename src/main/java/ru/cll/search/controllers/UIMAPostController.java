package ru.cll.search.controllers;

import org.apache.uima.UIMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cll.search.service.UIMAService;

import java.io.IOException;

/**
 * author: Nikita
 * since: 05.05.2014
 */
@Controller
public class UIMAPostController extends BaseController{
    @Autowired
    private UIMAService uimaService;

    @RequestMapping(value = "/testUima", method = {RequestMethod.GET, RequestMethod.POST})
    public String getAllAnnotation(@RequestParam String text) throws IOException, UIMAException {
        request.setAttribute("initialText", text);
        String annotationInfo = uimaService.getAllAnnotationsAsString(text);
        request.setAttribute("annotations", annotationInfo.replace("\n", "<br>"));
        return "templates/minimalUimaTemplate";
    }
}
