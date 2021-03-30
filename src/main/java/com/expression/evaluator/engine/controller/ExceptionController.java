package com.expression.evaluator.engine.controller;

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
import java.util.concurrent.ExecutionException;

/**
 * Exception Global Controller that helps convert exceptions into client friendly response.
 */
@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ScriptException.class)
    public ResponseEntity<Object> handleScriptException(
            ScriptException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex, "No Such Function in Script"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<Object> handleNoSuchMethodException(
            NoSuchMethodException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex, "No Such Function in Script"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedException(
            NoSuchMethodException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex, "Script Execution Interrupted"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<Object> handleExecutionException(
            NoSuchMethodException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorResponse(ex, "Script Execution Interrupted"), HttpStatus.NOT_ACCEPTABLE);
    }

    private Map<String, Object> buildErrorResponse(Exception ex, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("reason", ex.getLocalizedMessage());
        logger.error(ex);
        return body;
    }
}
