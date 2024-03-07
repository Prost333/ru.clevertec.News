package ru.clevertec.ManagementNews.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoggNewsAspectTest {
    private LoggNewsAspect loggNewsAspectTest;
    private ProceedingJoinPoint proceedingJoinPoint;
    private Signature signature;

    @BeforeEach
    public void setup() {
        loggNewsAspectTest = new LoggNewsAspect();
        proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        signature = mock(Signature.class);
    }

    @Test
    public void testLogMethodExecutionTime() throws Throwable {
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(proceedingJoinPoint.proceed()).thenReturn(null);

        Object result = loggNewsAspectTest.logMethodExecutionTime(proceedingJoinPoint);

        verify(proceedingJoinPoint, times(1)).proceed();
        assertEquals(null, result);
    }

    @Test
    public void testAfterUser() {
        Throwable e = new RuntimeException("Test exception");
        loggNewsAspectTest.afterUser(e);
    }
}
