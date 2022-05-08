package com.accenture.covid19stats.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private String code;
    private String message;

    public static ErrorDto fromCode(ErrorCode code, Object ...params) {
        return new ErrorDto(code.getName(),
                code.getMessage(params));
    }
}
