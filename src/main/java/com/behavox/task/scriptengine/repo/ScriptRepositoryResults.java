package com.behavox.task.scriptengine.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepositoryResults extends CrudRepository<CalculationResult, Integer> {
    CalculationResult findByHash(long hash);
    Boolean existsByHash(long hash);
}
