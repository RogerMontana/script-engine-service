package com.behavox.task.scriptengine.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface that helps to interact with DB.
 */
@Repository
public interface ScriptExecutionResultRepository extends CrudRepository<ExecutionResult, Integer> {
    Optional<ExecutionResult> findByHash(long hash);

    Collection<ExecutionResult> findAllByScriptName(String scriptName);

    void removeAllByScriptName(String scriptName);
}
