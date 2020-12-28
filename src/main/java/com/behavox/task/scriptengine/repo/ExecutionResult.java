package com.behavox.task.scriptengine.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table that keeps all execution results.
 */
@Entity
@Table(name = "EXECUTION_RESULTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionResult {

    @Id
    @Column(name = "hash", nullable = false)
    private Long hash;

    @Column(name = "script_name", length = 256, nullable = false)
    private String scriptName;

    @Column(name = "script", length = 256, nullable = false)
    private String script;

    @Column(name = "params", length = 256, nullable = false)
    private String params;

    @Column(name = "result", length = 256, nullable = false)
    private String result;
}
