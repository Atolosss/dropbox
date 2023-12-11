package com.dropbox.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ServiceExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleServiceExceptionHandler(final ServiceException e) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final UserError exception = new UserError(e.getMessage(),
                httpStatus,
                ZonedDateTime.now());
        return new ResponseEntity<>(exception, httpStatus);

    }

}
