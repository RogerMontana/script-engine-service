package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.repo.CalculationResult;
import com.behavox.task.scriptengine.repo.ScriptRepositoryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScriptService {
    private final ScriptEngine engine;
    private final ScriptRepositoryResults repositoryResults;
    private final Map<Integer, String> resultMap = new ConcurrentHashMap<>();

    public String eval(Input input) throws ScriptException, NoSuchMethodException {

        var hashCode = input.getDBHash();
        log.info("Hash {}", hashCode);

        if (repositoryResults.existsByHash(hashCode)) {
            String res = repositoryResults.findByHash(hashCode).getResult();
            log.info("results in DB key {}, val {}", hashCode, res);
            return res;
        }
        Object result = runOnEngine(input);
        String res = String.valueOf(result);
        repositoryResults.save(buildCalculationResultEntity(input, hashCode, res));
        return res;
    }

    private CalculationResult buildCalculationResultEntity(Input input, long hashCode, String res) {
        return new CalculationResult(hashCode, input.getFunctionPayload().trim(), Arrays.toString(input.getFunctionArgs()), res);
    }

    private Object runOnEngine(Input input) throws ScriptException, NoSuchMethodException {
        engine.eval(input.getFunctionPayload());
        Invocable inv = (Invocable) engine;
        Object result = inv.invokeFunction(input.getFunctionName(), input.getFunctionArgs());
        log.info("results going to be calculated from Input {}, result {}", input, result);
        return result;
    }

}
