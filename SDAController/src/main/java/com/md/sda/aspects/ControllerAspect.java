package com.md.sda.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {

    @Pointcut("execution(* com.md.sda.controller.SDAController.*(..))")
    public void allMethodsPointCut(){}//pointcut name

    @Before("allMethodsPointCut()")//applying pointcut on before advice
    public void argsAdvice(JoinPoint jp) {
        System.out.println("Starting method signature: "  + jp.getSignature());
    }


}

