package com.behavox.task.scriptengine.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionResultDTO {
    private String script;
    private String params;
    private String result;
}
