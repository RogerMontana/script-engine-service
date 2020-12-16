package com.behavox.task.scriptengine.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ScriptRepositoryResults extends CrudRepository<ExecutionResult, Integer> {
    Optional<ExecutionResult> findByHash(long hash);

    Collection<ExecutionResult> findAllByScriptName(String scriptName);

    void removeAllByScriptName(String scriptName);
}
