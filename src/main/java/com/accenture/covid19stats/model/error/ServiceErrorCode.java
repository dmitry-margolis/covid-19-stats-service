package com.accenture.covid19stats.model.error;

import org.springframework.http.HttpStatus;

public enum ServiceErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("Internal server error: {0}"),
    VALIDATION_ERROR("Invalid request: {0}", HttpStatus.BAD_REQUEST),

    NO_DATA_FOUND("Data not found for filter {0}", HttpStatus.NOT_FOUND);

    private final String messageTemplate;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    ServiceErrorCode(String messageTemplate, HttpStatus httpStatus) {
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    ServiceErrorCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
