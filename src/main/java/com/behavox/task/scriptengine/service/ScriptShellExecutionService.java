package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.dto.InputDto;
import com.behavox.task.scriptengine.repo.ExecutionResult;
import com.behavox.task.scriptengine.repo.ScriptExecutionResultRepository;
import groovy.lang.GroovyShell;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScriptShellExecutionService {
    private final ScriptExecutionResultRepository repositoryResults;

    public String executeAsync(InputDto inputDto) {
        ExecutorService myWorkers = Executors.newFixedThreadPool(1);
        var hashCode = inputDto.getDBHash();
        var result = "null";
        log.debug("Hash for an input {}", hashCode);

        Optional<ExecutionResult> calculationResult = repositoryResults.findByHash(hashCode);
        if (calculationResult.isPresent()) {
            ExecutionResult executionResult = calculationResult.get();
            log.info("results in DB key {}, val {}", hashCode, executionResult.getResult());
            return executionResult.getResult();
        }

        final CompletableFuture<ExecutionResult> scriptTask =
                CompletableFuture.supplyAsync(buildScriptExecTask(inputDto.getFunctionPayload()), myWorkers)
                        .thenApply(r -> repositoryResults.save(buildCalculationResultEntity(inputDto, hashCode, String.valueOf(r))))
                        .orTimeout(10, TimeUnit.SECONDS);

        try {
            result = String.valueOf(scriptTask.get().getResult());
        } catch (InterruptedException | ExecutionException e) {
            log.info("Kill the executor thread/s.");
            myWorkers.shutdownNow(); // this will interrupt the thread, catch the IntrExcep in thread and return the execution there
        }

        return result;
    }

    private Supplier<Object> buildScriptExecTask(String script) {
        return () -> execScript(script);
    }


    private Object execScript(String script) {

        var conf = new CompilerConfiguration();
        var customizer = new SecureASTCustomizer();
        customizer.setReceiversBlackList(Arrays.asList(System.class.getName()));
        conf.addCompilationCustomizers(customizer);
        var shell = new GroovyShell(conf);
        return shell.evaluate(script);
    }

    private ExecutionResult buildCalculationResultEntity(InputDto inputDto, long hashCode, String res) {
        return new ExecutionResult(hashCode,
                inputDto.getFunctionName().isEmpty() ? String.valueOf(hashCode) : inputDto.getFunctionName(),
                inputDto.getFunctionPayload().trim(),
                Arrays.toString(inputDto.getFunctionArgs()),
                res);
    }
}
