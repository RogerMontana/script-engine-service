package com.behavox.task.scriptengine.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Arrays;

/**
 * DTO that represents payload for given script and calculate payload hash.
 */
@Data
@JsonIgnoreProperties("dbHash")
public class InputDto {
    private final String functionName;
    private final String functionPayload;
    private final Object[] functionArgs;

    @Transient
    public long getDBHash() {
        var func = functionPayload.toLowerCase().replaceAll("\\s+", "");
        var name = functionName.toLowerCase().replaceAll("\\s+", "");
        var args = Arrays.toString(functionArgs).toLowerCase().replaceAll("\\s+", "");
        var str = func + name + args;
        return str.hashCode();
    }

}
