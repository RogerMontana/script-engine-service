package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.dto.InputDto;
import com.behavox.task.scriptengine.repo.ExecutionResult;
import com.behavox.task.scriptengine.repo.ScriptExecutionResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

/**
 * Service that helps to execute and get results for given script.
 * Duplicate calls - read previous DB results.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScriptExecutionService {
    private final ScriptEngine engine;
    private final ScriptExecutionResultRepository repositoryResults;

    @Transactional
    public String eval(InputDto inputDto) throws ScriptException, NoSuchMethodException {
        var hashCode = inputDto.getDBHash();
        log.debug("Hash for an input {}", hashCode);

        Optional<ExecutionResult> calculationResult = repositoryResults.findByHash(hashCode);
        if (calculationResult.isPresent()) {
            String result = calculationResult.get().getResult();
            log.info("results in DB key {}, val {}", hashCode, result);
            return result;
        }
        Object result = runOnEngine(inputDto);
        String res = String.valueOf(result);
        repositoryResults.save(buildCalculationResultEntity(inputDto, hashCode, res));
        return res;
    }

    private ExecutionResult buildCalculationResultEntity(InputDto inputDto, long hashCode, String res) {
        return new ExecutionResult(hashCode,
                inputDto.getFunctionName().isEmpty() ? String.valueOf(hashCode) : inputDto.getFunctionName(),
                inputDto.getFunctionPayload().trim(),
                Arrays.toString(inputDto.getFunctionArgs()),
                res);
    }

    private Object runOnEngine(InputDto inputDto) throws ScriptException, NoSuchMethodException {
        if (inputDto.getFunctionName().isEmpty() && inputDto.getFunctionArgs().length == 0) {
            return engine.eval(inputDto.getFunctionPayload());
        }
        engine.eval(inputDto.getFunctionPayload());
        Invocable inv = (Invocable) engine;
        var result = inv.invokeFunction(inputDto.getFunctionName(), inputDto.getFunctionArgs());
        log.info("results going to be calculated from Input {}, result {}", inputDto, result);
        return result;
    }

}
