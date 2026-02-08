package com.IH.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    // Срез: все методы во всех контроллерах пакета com.IH.controller
    @Pointcut("execution(* com.IH.controller..*(..))")
    public void controllerMethods() {}

    // Выполнить перед входом в метод
    @Before("controllerMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();

        System.out.println("\n===== AOP LOG START =====");
        System.out.println("Вызван класс: " + className);
        System.out.println("Вызван метод: " + methodName);
        System.out.println("Аргументы: " + Arrays.toString(args));
        System.out.println("===== AOP LOG END =====\n");
    }
}
