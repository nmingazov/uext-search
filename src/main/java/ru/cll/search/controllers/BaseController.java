package ru.cll.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Base class for all controllers
 * author: Nikita
 * since: 05.05.2014
 */
@Controller
public class BaseController {
    @Autowired
    protected HttpServletRequest request;
}
