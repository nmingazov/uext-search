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

    protected String badRequest(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), message);
        return "";
    }

    protected String notFound(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), message);
        return "";
    }
}
