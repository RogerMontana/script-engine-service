package com.behavox.task.scriptengine;

import com.behavox.task.scriptengine.dto.ExecutionResultDTO;
import com.behavox.task.scriptengine.dto.InputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BehavoxScriptEngineApplication.class)
@AutoConfigureMockMvc
public class BehavoxScriptEngineApplicationTests {

    public static final String FUNCTION_NAME = "fib";
    public static final String FUNCTION_PAYLOAD = "def fib(n) { n < 2 ? n : fib(n - 2) + fib(n - 1) }";
    public static final Object[] ARGS = new Object[]{3};
    public static final Object[] ARGS_2 = new Object[]{4};
    public static final Object[] ARGS_3 = new Object[]{5};

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    public void shouldLoginAndRedirectToSwagger() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("user").password("pass");
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection()); // Redirect to swagger page
    }

    @Test
    @WithMockUser("user")
    public void shouldRunSingleScriptPayload() throws Exception {
        Object[] args = {3};
        String functionPayload = "def fib(n) { n < 2 ? n : fib(n - 2) + fib(n - 1) }";
        String functionName = "fib";

        mvc.perform(post("/api/v1/engine/eval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new InputDto(functionName,
                        functionPayload,
                        args))))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ExecutionResultDTO(functionPayload,
                                Collections.singletonList(3).toString(),
                                String.valueOf(2)))
                        )
                );
    }


    @Test
    @WithMockUser("user")
    public void shouldExecuteBatchPayload() throws Exception {

        var input1 = new InputDto(FUNCTION_NAME,
                FUNCTION_PAYLOAD,
                ARGS);
        var input2 = new InputDto(FUNCTION_NAME,
                FUNCTION_PAYLOAD,
                ARGS_2);
        var input3 = new InputDto(FUNCTION_NAME,
                FUNCTION_PAYLOAD,
                ARGS_3);


        mvc.perform(post("/api/v1/engine/evalBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(input1, input2, input3))))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content()
                        .json(
                                objectMapper.writeValueAsString(
                                        Arrays.asList(
                                                new ExecutionResultDTO(FUNCTION_PAYLOAD,
                                                        Collections.singletonList(3).toString(),
                                                        String.valueOf(2)),
                                                new ExecutionResultDTO(FUNCTION_PAYLOAD,
                                                        Collections.singletonList(4).toString(),
                                                        String.valueOf(3)),
                                                new ExecutionResultDTO(FUNCTION_PAYLOAD,
                                                        Collections.singletonList(5).toString(),
                                                        String.valueOf(5))
                                        )
                                )
                        )
                );

    }


    @Test
    @WithMockUser("user")
    public void shouldNotAllowDeleteExecutionResultsByAdmin() throws Exception {
        shouldRunSingleScriptPayload();
        mvc.perform(delete("/api/v1/engine/results/byScriptName/fib"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("user")
    public void shouldAllowToGetExecutionResultsByAdmin() throws Exception {
        shouldRunSingleScriptPayload();
        mvc.perform(get("/api/v1/engine/results/byScriptName/fib"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(
                                objectMapper.writeValueAsString(
                                        Arrays.asList(
                                                new ExecutionResultDTO(FUNCTION_PAYLOAD,
                                                        Collections.singletonList(3).toString(),
                                                        String.valueOf(2))
                                        )
                                )
                        )
                );;
    }
}
