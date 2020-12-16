package com.behavox.task.scriptengine.controller;

import com.behavox.task.scriptengine.service.Input;
import com.behavox.task.scriptengine.service.ScriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/engine")
public class ScriptEngineController {

    private final ScriptService scriptService;

    @PostMapping("/eval")
    public ExecutionResultDTO evalResult(@RequestBody Input input) throws ScriptException, NoSuchMethodException {
        
        var result = scriptService.eval(input);
        return new ExecutionResultDTO(input.getFunctionPayload(),
                Arrays.toString(input.getFunctionArgs()),
                result);
    }
}
