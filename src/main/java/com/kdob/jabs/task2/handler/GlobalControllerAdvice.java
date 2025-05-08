package com.kdob.jabs.task2.handler;

import com.kdob.jabs.task2.dto.ErrorMessage;
import com.kdob.jabs.task2.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(final CustomerNotFoundException customerNotFoundException) {
        return new ErrorMessage(customerNotFoundException.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestException(final Exception exception) {
        return new ErrorMessage(exception.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

}
