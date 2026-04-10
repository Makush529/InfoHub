package com.IH.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.IH.controller..*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.debug("===== AOP LOG START =====");
        log.debug("Вызван класс: " + className);
        log.debug("Вызван метод: " + methodName);
        log.debug("Аргументы: " + Arrays.toString(args));
        log.debug("===== AOP LOG END =====");
    }
}
