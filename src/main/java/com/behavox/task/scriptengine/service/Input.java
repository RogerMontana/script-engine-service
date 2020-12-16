package com.behavox.task.scriptengine.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Arrays;

@Data
public class Input {
    private final String functionName;
    private final String functionPayload;
    private final Object[] functionArgs;

    @Transient
    public long getDBHash() {
        var func = functionPayload.toLowerCase().replaceAll("\\s+","");
        var name = functionName.toLowerCase().replaceAll("\\s+","");
        var args = Arrays.toString(functionArgs).replaceAll("\\s+","");
        var str = func + name + args;
        return str.hashCode();
    }

}
