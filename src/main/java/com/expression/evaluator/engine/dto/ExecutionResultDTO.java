package com.expression.evaluator.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO that represents execution result for given script.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionResultDTO {
    private String script;
    private String params;
    private String result;
}
