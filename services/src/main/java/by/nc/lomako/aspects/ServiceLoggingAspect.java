/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
@Aspect
public class ServiceLoggingAspect {
    @Pointcut("within(by.nc.lomako.services.impl.*)")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logServiceMethods(JoinPoint joinPoint) throws Throwable {

        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(joinPoint.getSignature().toShortString());
        stringBuilder.append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(stringBuilder::append);
        stringBuilder.append(")");

        log.debug(joinPoint.getSignature().toShortString() + stringBuilder.toString());
    }

    @AfterThrowing(
            pointcut = "serviceMethods()",
            throwing = "ex"
    )
    public void logServiceExceptions(JoinPoint joinPoint, Throwable ex) {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        log.debug("Service exception: " + ex.getClass().getName());
    }

}
