package com.draghici.hub.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* provides a global exception handling for all controllers in the application. */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /*
     * handles the custom 'ProductException' exceptions.
     * Return the received error code.
     */
    @ExceptionHandler(ProductException.class)
    ResponseEntity<HubException> handleProductException(ProductException ex) {
        logger.warn(ex.getMessage());

        var exceptionResponse = new HubException(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getErrorCode()).body(exceptionResponse);
    }

    /*
     * handles the generic runtime exceptions.
     * Return error code of 500 (Internal Server Error).
     * */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HubException> handleGeneralException(RuntimeException ex) {
        logger.error(ex.getMessage(), ex.getCause());

        var exceptionResponse = new HubException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}