package ru.cll.search.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HelloController extends BaseController {
    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome() {
		request.setAttribute("message", "Hello world!");
		return "hello";
	}
}