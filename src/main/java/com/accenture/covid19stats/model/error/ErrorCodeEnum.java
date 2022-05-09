package com.accenture.covid19stats.model.error;

import org.springframework.http.HttpStatus;

public enum ErrorCodeEnum implements ErrorCode {
    INTERNAL_SERVER_ERROR("Internal server error: {0}"),
    VALIDATION_ERROR("Invalid request: {0}", HttpStatus.BAD_REQUEST),

    FETCHING_COVID19_API_ERROR("Error fetching data from API"),
    NO_DATA_FOUND("Data not found for filter {0}", HttpStatus.NOT_FOUND);

    private final String messageTemplate;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    ErrorCodeEnum(String messageTemplate, HttpStatus httpStatus) {
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    ErrorCodeEnum(String messageTemplate) {
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
