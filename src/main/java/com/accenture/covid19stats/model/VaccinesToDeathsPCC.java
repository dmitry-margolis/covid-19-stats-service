package com.accenture.covid19stats.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Pearson correlation coefficient between vaccination and fatality rates")
public class VaccinesToDeathsPCC {
    @Schema(description = "PCC value in range [-1.0;1.0]", example = "-0.65")
    private BigDecimal coefficient;
}
