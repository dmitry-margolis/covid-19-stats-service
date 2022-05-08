package com.accenture.covid19stats.model.error;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.text.MessageFormat;

public interface ErrorCode extends Serializable {
    String getName();
    String getMessageTemplate();
    HttpStatus getHttpStatus();

    default String getMessage(Object ...params) {
        return MessageFormat.format(getMessageTemplate(), params);
    }
}
