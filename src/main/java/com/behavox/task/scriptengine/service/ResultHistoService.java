package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.dto.ExecutionResultDTO;
import com.behavox.task.scriptengine.repo.ScriptRepositoryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultHistoService {
    private final ScriptRepositoryResults repositoryResults;

    public List<ExecutionResultDTO> getAllResultsByScriptName(String scriptName) {
        log.debug("going to get histo data by script with name {}", scriptName);
        return repositoryResults.findAllByScriptName(scriptName)
                .stream()
                .map(r-> new ExecutionResultDTO(r.getScript(), r.getParams(), r.getResult()))
                .collect(Collectors.toList());
    }

    public void cleanHistoryForScript(String scriptName) {
        log.debug("going to clean all histo data by script with name {}", scriptName);
        repositoryResults.removeAllByScriptName(scriptName);
    }
}
