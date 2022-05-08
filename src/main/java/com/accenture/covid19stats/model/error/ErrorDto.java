package com.accenture.covid19stats.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Application error")
public class ErrorDto {
    @Schema(title = "Error code", example = "INTERNAL_SERVER_ERROR")
    private String code;
    @Schema(title = "Error message", example = "Internal server error")
    private String message;

    public static ErrorDto fromCode(ErrorCode code, Object ...params) {
        return new ErrorDto(code.getName(),
                code.getMessage(params));
    }
}
