package com.accenture.covid19stats.controller.common;

import com.accenture.covid19stats.model.error.ErrorDto;
import com.accenture.covid19stats.model.error.ServiceBusinessException;
import com.accenture.covid19stats.model.error.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestControllerErrorHandler {

    @ExceptionHandler(ServiceBusinessException.class)
    public ResponseEntity<ErrorDto> handleRkkException(ServiceBusinessException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorDto(ex.getCode().getName(), ex.getMessage()),
                ex.getCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> sb.append(String.format("Field %s.%s: %s; ",
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage())));
        return new ErrorDto(ErrorCodeEnum.VALIDATION_ERROR.getName(),
                ErrorCodeEnum.VALIDATION_ERROR.getMessage(sb.toString()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleAnyException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        return ErrorDto.fromCode(ErrorCodeEnum.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
