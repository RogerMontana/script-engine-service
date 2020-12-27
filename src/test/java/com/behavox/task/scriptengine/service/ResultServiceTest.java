package com.behavox.task.scriptengine.service;

import com.behavox.task.scriptengine.TestConfiguration;
import com.behavox.task.scriptengine.repo.ScriptExecutionResultRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest extends TestConfiguration {

    @Mock
    private ScriptExecutionResultRepository scriptExecutionResultRepository;

    @InjectMocks
    private ScriptExecutionService scriptExecutionService;
}
