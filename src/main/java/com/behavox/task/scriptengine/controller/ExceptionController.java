package com.behavox.task.scriptengine.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.script.ScriptException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exception Global Controller that helps convert exceptions into client friendly response.
 */
@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ScriptException.class)
    public ResponseEntity<Object> handleScriptException(
            ScriptException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<Object> handleNoSuchMethodException(
            NoSuchMethodException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex), HttpStatus.NOT_ACCEPTABLE);
    }

    private Map<String, Object> buildErrorResponse(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "No Such Function in Script");
        body.put("reason", ex.getLocalizedMessage());
        logger.error(ex);
        return body;
    }
}
