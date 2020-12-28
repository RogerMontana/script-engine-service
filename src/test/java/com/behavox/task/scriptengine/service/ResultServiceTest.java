package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.TestConfiguration;
import com.behavox.task.scriptengine.dto.ExecutionResultDTO;
import com.behavox.task.scriptengine.repo.ExecutionResult;
import com.behavox.task.scriptengine.repo.ScriptExecutionResultRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest extends TestConfiguration {

    public static final long HASH = 123456L;

    @Mock
    private ScriptExecutionResultRepository scriptExecutionResultRepository;

    @InjectMocks
    private ResultService resultService;

    @Test
    public void shouldReadResultsByScriptName() {

        Mockito.when(scriptExecutionResultRepository.findAllByScriptName(FUNCTION_NAME_FIB)).thenReturn(List.of(
                new ExecutionResult(HASH, FUNCTION_NAME_FIB, FUNCTION_PAYLOAD_FIB, Arrays.toString(ARGS_2), "10")
        ));

        ExecutionResultDTO executionResultDTO = new ExecutionResultDTO(FUNCTION_PAYLOAD_FIB,
                Arrays.toString(ARGS_2),
                "10");
        List<ExecutionResultDTO> allResultsByScriptName = resultService.getAllResultsByScriptName(FUNCTION_NAME_FIB);

        verify(scriptExecutionResultRepository, times(1)).findAllByScriptName(FUNCTION_NAME_FIB);
        assertEquals(Collections.singletonList(executionResultDTO), allResultsByScriptName);

    }

    @Test
    public void shouldGetRemoveResults() {
        Mockito.when(scriptExecutionResultRepository.findAllByScriptName(FUNCTION_NAME_FIB)).thenReturn(Lists.emptyList());

        resultService.cleanHistoryForScript(FUNCTION_NAME_FIB);

        verify(scriptExecutionResultRepository, times(1)).removeAllByScriptName(FUNCTION_NAME_FIB);
        assertEquals(0, resultService.getAllResultsByScriptName(FUNCTION_NAME_FIB).size());
    }
}
