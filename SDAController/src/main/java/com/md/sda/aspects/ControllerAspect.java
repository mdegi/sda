package com.md.sda.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Date;

@Aspect
@Component
public class ControllerAspect extends SDAAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.md.sda.controller.SDAController.*(..))")
    public void allMethodsPointCut(){}//pointcut name

    @Before("allMethodsPointCut()")//applying pointcut on before advice
    public void allMethodsBeforeAdvice(JoinPoint joinPoint) {
        LOGGER.info(df.format(new Date()) + " Start: " + joinPoint.getSignature());
    }

    @After("allMethodsPointCut()")//applying pointcut on before advice
    public void allMethodsAfterAdvice(JoinPoint joinPoint) {
        LOGGER.info(df.format(new Date()) + " End: " + joinPoint.getSignature());
    }

    @AfterThrowing(value = "allMethodsPointCut()", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) throws Throwable {
        if (e instanceof ConstraintViolationException) {
            LOGGER.error(df.format(new Date()) + " " + joinPoint.getSignature() + "Invalid parameter input or parameter does not match defined pattern\n" + e.getMessage());
        } else {
            LOGGER.error(df.format(new Date()) + " " + joinPoint.getSignature() + "Unknown exception occurred" + e.getMessage());
        }
    }

}

