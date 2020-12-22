package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.TestConstants;
import com.behavox.task.scriptengine.dto.InputDto;
import com.behavox.task.scriptengine.repo.ScriptExecutionResultRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class ScriptExecutionServiceTest extends TestConstants {

    @Mock
    private ScriptExecutionResultRepository scriptExecutionResultRepository;

    @InjectMocks
    private ScriptExecutionService scriptExecutionService;


    @Test
    @Ignore
    public void shouldCalculateResults() throws ScriptException, NoSuchMethodException {

        Invocable inv = Mockito.mock(Invocable.class);
        ScriptEngine engine = Mockito.mock(ScriptEngine.class);

        Mockito.when(inv.invokeFunction(anyString(), anyString())).thenReturn("anyString()");
        Mockito.when(engine.eval(FUNCTION_PAYLOAD_FIB)).thenReturn(anyString());

        InputDto inputDto = new InputDto(FUNCTION_NAME_FIB,
                FUNCTION_PAYLOAD_FIB,
                ARGS);
        scriptExecutionService.eval(inputDto);

        verify(scriptExecutionResultRepository, times(1)).findByHash(inputDto.getDBHash());
        verify(scriptExecutionResultRepository, times(1)).save(any());
    }

    @Test
    @Ignore
    public void shouldGetResultsFromDbIfPreviousPayloadStillSame() throws ScriptException, NoSuchMethodException {
        InputDto inputDto = new InputDto(FUNCTION_NAME_FIB,
                FUNCTION_PAYLOAD_FIB,
                ARGS);
        scriptExecutionService.eval(inputDto);
        verify(scriptExecutionResultRepository, times(1)).findByHash(inputDto.getDBHash());
        verify(scriptExecutionResultRepository, times(0)).save(any());
    }

    @Test
    @Ignore
    public void shouldThrowNoSuchMethodException() {

    }

    @Test
    @Ignore
    public void shouldThrowScriptExceptionException() {

    }
}
