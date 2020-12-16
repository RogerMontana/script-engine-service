package com.behavox.task.scriptengine.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CalculatedResults")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResult {

        @Id
        @Column(name = "hash", nullable = false)
        private Long hash;

        @Column(name = "script", length = 256, nullable = false)
        private String script;

        @Column(name = "params", length = 256, nullable = false)
        private String params;

        @Column(name = "result", length = 256, nullable = false)
        private String result;
}
