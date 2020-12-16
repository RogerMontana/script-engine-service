package com.behavox.task.scriptengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
public class Config {

    @Bean
    ScriptEngine scriptEngine() {
        return new ScriptEngineManager().getEngineByName("groovy");
    }

}
