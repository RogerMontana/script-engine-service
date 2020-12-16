package com.behavox.task.scriptengine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
@Slf4j
public class ScriptEngineConfiguration {

    public static final String GROOVY = "groovy";
    public static final String NASHORN = "nashorn";

    @Bean
    @ConditionalOnProperty(
            value="engine.groovy",
            havingValue = "true",
            matchIfMissing = true)
    ScriptEngine scriptEngineGroovy() {
        log.info("Load Groovy Engine");
        return new ScriptEngineManager().getEngineByName(GROOVY);
    }

    @Bean
    @ConditionalOnProperty(
            value="engine.javascript",
            havingValue = "true",
            matchIfMissing = false)
    ScriptEngine scriptEngineNashorn() {
        log.info("Load Nashorn Engine");
        return new ScriptEngineManager().getEngineByName(NASHORN);
    }

}
