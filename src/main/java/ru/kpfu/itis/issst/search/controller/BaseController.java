package ru.kpfu.itis.issst.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base class for all controllers
 * author: Nikita
 * since: 05.05.2014
 */
public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    protected String badRequest(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "badRequest";
    }

    protected String notFound(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "notFound";
    }
}
