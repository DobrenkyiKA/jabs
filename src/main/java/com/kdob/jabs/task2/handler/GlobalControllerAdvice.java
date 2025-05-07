package com.kdob.jabs.task2.handler;

import com.kdob.jabs.task2.dto.ErrorMessageDto;
import com.kdob.jabs.task2.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageDto handleNotFoundException(final CustomerNotFoundException customerNotFoundException) {
        return new ErrorMessageDto(customerNotFoundException.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleBadRequestException(final Exception exception) {
        return new ErrorMessageDto(exception.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

}
