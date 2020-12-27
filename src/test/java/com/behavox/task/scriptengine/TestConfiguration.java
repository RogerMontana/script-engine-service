package com.behavox.task.scriptengine;

public class TestConfiguration {

    protected static final String FUNCTION_NAME_FIB = "fib";
    protected static final String FUNCTION_PAYLOAD_FIB = "def fib(n) { n < 2 ? n : fib(n - 2) + fib(n - 1) }";
    protected static final Object[] ARGS = new Object[]{3};
    protected static final Object[] ARGS_2 = new Object[]{4};
    protected static final Object[] ARGS_3 = new Object[]{5};

}
