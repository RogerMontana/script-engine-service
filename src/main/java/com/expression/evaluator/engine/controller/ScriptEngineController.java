package com.expression.evaluator.engine.controller;

import com.expression.evaluator.engine.dto.ExecutionResultDTO;
import com.expression.evaluator.engine.dto.InputDto;
import com.expression.evaluator.engine.service.ResultService;
import com.expression.evaluator.engine.service.ScriptExecutionService;
import com.expression.evaluator.engine.service.ScriptShellExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ScriptShellExecutionService scriptShellExecutionService;
    private final ResultService histoService;

    @PostMapping(value = "/evalShellOne", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExecutionResultDTO executeAsyncSingeFunction(@RequestBody InputDto inputDto) {
        log.debug("Received Request with Input {}", inputDto);
        var result = scriptShellExecutionService.executeAsync(inputDto);
        return new ExecutionResultDTO(inputDto.getFunctionPayload(),
                Arrays.toString(inputDto.getFunctionArgs()),
                result);
    }

    @PostMapping(value = "/evalOne", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExecutionResultDTO executeSingeFunction(@RequestBody InputDto inputDto) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Input {}", inputDto);
        var result = scriptExecutionService.eval(inputDto);
        return new ExecutionResultDTO(inputDto.getFunctionPayload(),
                Arrays.toString(inputDto.getFunctionArgs()),
                result);
    }

    @PostMapping(value = "/evalMany", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExecutionResultDTO>> executeManyFunctions(@RequestBody List<InputDto> inputDtos) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Batch Input {}", inputDtos);
        List<ExecutionResultDTO> result = new ArrayList<>();
        for (InputDto input : inputDtos) {
            result.add(executeSingeFunction(input));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/results/byScriptName/{scriptName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExecutionResultDTO>> getExecutionResultsByScriptName(@PathVariable String scriptName) {
        var result = histoService.getAllResultsByScriptName(scriptName);
        log.debug("Get Requests by scriptName: {} result num: {}", scriptName, result.size());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/results/byScriptName/{scriptName}")
    public void deleteExecutionResultsByScriptName(@PathVariable String scriptName) {
        log.debug("Clear Results by scriptName: {}", scriptName);
        histoService.cleanHistoryForScript(scriptName);
    }
}
