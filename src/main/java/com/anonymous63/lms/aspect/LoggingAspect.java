package com.anonymous63.lms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.anonymous63.lms.controller..*)")
    public void controllerMethods() {
    }

    @Pointcut("within(com.anonymous63.lms.service..*)")
    public void serviceMethods() {
    }

    // 🔹 Log before execution
    @Before("controllerMethods() || serviceMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("➡️ Entering {} with arguments {}", method, Arrays.toString(args));
    }

    // 🔹 Log after successful execution
    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        log.info("✅ Exiting {} with result {}", method, result);
    }

    // 🔹 Log if an exception is thrown
    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) {
        String method = joinPoint.getSignature().toShortString();
        log.error("❌ Exception in {}: {}", method, ex.getMessage(), ex);
    }

    // 🔹 (Optional) Log execution time
    @Around("controllerMethods() || serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed(); // execute the method
        long elapsed = System.currentTimeMillis() - start;
        log.info("⏱ {} executed in {} ms", pjp.getSignature().toShortString(), elapsed);
        return result;
    }
}
