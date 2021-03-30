package com.expression.evaluator.engine.service;

import com.expression.evaluator.engine.dto.ExecutionResultDTO;
import com.expression.evaluator.engine.repo.ScriptExecutionResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that helps to get and remove results for given script.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ResultService {
    private final ScriptExecutionResultRepository repositoryResults;

    @Transactional(readOnly = true)
    public List<ExecutionResultDTO> getAllResultsByScriptName(String scriptName) {
        log.debug("going to get histo data by script with name {}", scriptName);
        return repositoryResults.findAllByScriptName(scriptName)
                .stream()
                .map(r -> new ExecutionResultDTO(r.getScript(), r.getParams(), r.getResult()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cleanHistoryForScript(String scriptName) {
        log.debug("going to clean all histo data by script with name {}", scriptName);
        repositoryResults.removeAllByScriptName(scriptName);
    }
}
