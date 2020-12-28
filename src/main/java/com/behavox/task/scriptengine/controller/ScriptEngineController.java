package com.behavox.task.scriptengine.controller;

import com.behavox.task.scriptengine.dto.ExecutionResultDTO;
import com.behavox.task.scriptengine.dto.InputDto;
import com.behavox.task.scriptengine.service.ResultService;
import com.behavox.task.scriptengine.service.ScriptExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ScriptEngineController API v1.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/engine")
@Slf4j
public class ScriptEngineController {

    private final ScriptExecutionService scriptExecutionService;
    private final ResultService histoService;

    @PostMapping("/evalOne")
    public ExecutionResultDTO evalOne(@RequestBody InputDto inputDto) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Input {}", inputDto);
        var result = scriptExecutionService.eval(inputDto);
        return new ExecutionResultDTO(inputDto.getFunctionPayload(),
                Arrays.toString(inputDto.getFunctionArgs()),
                result);
    }

    @PostMapping("/evalMany")
    public ResponseEntity<List<ExecutionResultDTO>> evalMany(@RequestBody List<InputDto> inputDtos) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Batch Input {}", inputDtos);
        List<ExecutionResultDTO> result = new ArrayList<>();
        for (InputDto input : inputDtos) {
            result.add(evalOne(input));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/results/byScriptName/{scriptName}")
    public ResponseEntity<List<ExecutionResultDTO>> getExecutionHisto(@PathVariable String scriptName) {
        var result = histoService.getAllResultsByScriptName(scriptName);
        log.debug("Get Requests by scriptName: {} result num: {}", scriptName, result.size());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/results/byScriptName/{scriptName}")
    public void deleteResults(@PathVariable String scriptName) {
        log.debug("Clear Results by scriptName: {}", scriptName);
        histoService.cleanHistoryForScript(scriptName);
    }
}
