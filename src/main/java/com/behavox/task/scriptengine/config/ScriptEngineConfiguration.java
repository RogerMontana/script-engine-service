package com.behavox.task.scriptengine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
@Slf4j
public class ScriptEngineConfiguration {

    public static final String GROOVY = "groovy";
    public static final String NASHORN = "nashorn";

    @Bean
    @ConditionalOnProperty(
            value = "engine.groovy",
            havingValue = "true",
            matchIfMissing = true)
    public ScriptEngine scriptEngineGroovy() {
        log.info("Load Groovy Engine");
        ScriptEngine engineByName = new ScriptEngineManager().getEngineByName(GROOVY);
        var bindings = engineByName.createBindings();
        engineByName.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return engineByName;
    }

    @Bean
    @ConditionalOnProperty(
            value = "engine.javascript",
            havingValue = "true",
            matchIfMissing = false)
    public ScriptEngine scriptEngineNashorn() {
        log.info("Load Nashorn Engine");
        var engineByName = new ScriptEngineManager().getEngineByName(NASHORN);
        var bindings = engineByName.createBindings();
        engineByName.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return engineByName;
    }

}
