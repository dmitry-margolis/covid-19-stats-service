package com.accenture.covid19stats.model.error;

import lombok.Getter;

@Getter
public class ServiceBusinessException extends RuntimeException {
    private final ErrorCode code;

    public ServiceBusinessException(ErrorCode code, Throwable cause, Object ...params) {
        super(code.getMessage(params), cause);
        this.code = code;
    }

    public ServiceBusinessException(ErrorCode code, Object ...params) {
        super(code.getMessage(params));
        this.code = code;
    }
}
