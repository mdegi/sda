package com.md.sda.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class ControllerAspect extends SDAAspect {

    @Pointcut("execution(* com.md.sda.controller.SDAController.*(..))")
    public void allMethodsPointCut(){}//pointcut name

    @Before("allMethodsPointCut()")//applying pointcut on before advice
    public void allMethodsBeforeAdvice(JoinPoint joinPoint) {
        System.out.println("----------- Starting method signature: "  + joinPoint.getSignature() + " Time: " + df.format(new Date()));
    }

    @After("allMethodsPointCut()")//applying pointcut on before advice
    public void allMethodsAfterAdvice(JoinPoint joinPoint) {
        System.out.println("----------- End method signature: "  + joinPoint.getSignature() + "      Time: " + df.format(new Date()));
    }

    @AfterThrowing(value = "allMethodsPointCut()", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        System.out.println("Exception caught: " + joinPoint.getSignature() + " -> " + e);
    }

}

