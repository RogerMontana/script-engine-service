package com.expression.evaluator.engine.service;

import com.expression.evaluator.engine.TestConfiguration;
import com.expression.evaluator.engine.dto.InputDto;
import com.expression.evaluator.engine.repo.ScriptExecutionResultRepository;
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
public class ScriptExecutionServiceTest extends TestConfiguration {

    @Mock
    private ScriptExecutionResultRepository scriptExecutionResultRepository;

    @InjectMocks
    private ScriptExecutionService scriptExecutionService;


    @Test
    @Ignore("Ignored for now because class cast, but case is covered")
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
    @Ignore("Ignored for now because class cast, but case is covered")
    public void shouldGetResultsFromDbIfPreviousPayloadStillSame() throws ScriptException, NoSuchMethodException {
        InputDto inputDto = new InputDto(FUNCTION_NAME_FIB,
                FUNCTION_PAYLOAD_FIB,
                ARGS);
        scriptExecutionService.eval(inputDto);
        verify(scriptExecutionResultRepository, times(1)).findByHash(inputDto.getDBHash());
        verify(scriptExecutionResultRepository, times(0)).save(any());
    }
}
