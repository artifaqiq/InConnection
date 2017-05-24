/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Lomako
 * @version 1.0
 */
@Aspect
@Component
public class SecurityLoggingAspect {

    @Before("execution(* by.nc.lomako.security.handlers.*.*(..))")
    public void logSecurityMethods(JoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        String message = joinPoint.getSignature().toShortString() +
                " Args: " +
                Arrays.stream(joinPoint.getArgs())
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
        log.warn(message);

    }

}
