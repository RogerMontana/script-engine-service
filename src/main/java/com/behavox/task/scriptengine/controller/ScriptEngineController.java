package com.behavox.task.scriptengine.controller;

import com.behavox.task.scriptengine.dto.ExecutionResultDTO;
import com.behavox.task.scriptengine.dto.InputDto;
import com.behavox.task.scriptengine.service.ResultHistoService;
import com.behavox.task.scriptengine.service.ScriptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/engine")
@Slf4j
public class ScriptEngineController {

    private final ScriptService scriptService;
    private final ResultHistoService histoService;

    @PostMapping("/eval")
    public ExecutionResultDTO evalResult(@RequestBody InputDto inputDto) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Input {}", inputDto);
        var result = scriptService.eval(inputDto);
        return new ExecutionResultDTO(inputDto.getFunctionPayload(),
                Arrays.toString(inputDto.getFunctionArgs()),
                result);
    }

    @PostMapping("/evalBatch")
    public ResponseEntity<List<ExecutionResultDTO>> evalBatchResult(@RequestBody List<InputDto> inputDtos) throws ScriptException, NoSuchMethodException {
        log.debug("Received Request with Batch Input {}", inputDtos);
        List<ExecutionResultDTO> result = new ArrayList<>();
        for (InputDto input:inputDtos) {
            result.add(evalResult(input));
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
