/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Lomako
 * @version 1.0
 */

@Aspect
@Component
public class DaoLoggingAspects {

    @Pointcut("within(by.nc.lomako.dao.impl.*)")
    public void daoMethods() {
    }

    @Around("daoMethods()")
    public Object logDaoMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(joinPoint.getSignature().toShortString());
        stringBuilder.append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(stringBuilder::append);
        stringBuilder.append(")");

        log.debug(joinPoint.getSignature().toShortString() + stringBuilder.toString());

        return joinPoint.proceed();
    }

    @AfterThrowing(
            pointcut = "daoMethods()",
            throwing = "ex"
    )
    public void logDaoExceptions(JoinPoint joinPoint, Throwable ex) {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());

        log.error("Dao exception", ex);
    }

}
