/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Lomako
 * @version 1.0
 */
@Aspect
@Component
public class ControllerLoggingAspect {
    @Around("execution(* by.nc.lomako.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        Object result = joinPoint.proceed();

        String message = joinPoint.getSignature().toShortString() +
                " Args: " +
                Arrays.stream(joinPoint.getArgs())
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                " Response: " + result.toString();

        log.debug(message);

        return result;
    }

    @Around("execution(* by.nc.lomako.controller.exceptions.*.*(..))")
    public Object logExceptionHandlerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        Object result = joinPoint.proceed();

        String message = joinPoint.getSignature().toShortString() +
                " Args: " +
                Arrays.stream(joinPoint.getArgs())
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                " Response: " + result.toString();

        log.info(message);

        return result;
    }

    @Around("execution(* by.nc.lomako.controller.exceptions.ExceptionHandler.noBindExceptionHandler(..))")
    public Object logNoBindExceptionHandlerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        Object result = joinPoint.proceed();

        String message = joinPoint.getSignature().toShortString() +
                " Args: " +
                Arrays.stream(joinPoint.getArgs())
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                " Response: " + result.toString();

        log.error(message);

        return result;
    }

}
