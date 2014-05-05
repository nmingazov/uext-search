package ru.cll.search;

import org.apache.uima.UIMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.cll.search.service.UIMAService;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class HelloController {
	@Autowired
    private UIMAService uimaService;

    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

    @RequestMapping(value = "mytest", method = {RequestMethod.GET, RequestMethod.POST})
    public String oneTryModel(@RequestParam String text, ModelMap model) throws IOException, UIMAException {
        model.addAttribute("initialText", text);
        String annotationInfo = uimaService.getAllAnnotationsAsString(text);
        model.addAttribute("annotations", annotationInfo.replace("\n", "<br>"));
        return "uimatest";
    }
}