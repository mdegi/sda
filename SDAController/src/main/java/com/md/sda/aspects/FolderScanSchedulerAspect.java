package com.md.sda.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class FolderScanSchedulerAspect extends SDAAspect {

    @Pointcut("execution(* com.md.sda.schedulingTasks.FolderScanScheduler.*(..))")
    private void allMethods() {}

    @Around("allMethods()")
    public void logDurationAdvice(ProceedingJoinPoint joinPoint) {
        System.out.println("----------- Starting method signature: "  + joinPoint.getSignature() + " Time: " + df.format(new Date()));
        try {
            joinPoint.proceed();
        } catch (Throwable t) {
            System.out.println("Something Happened here ..... Pls check" + joinPoint.getSignature());
        }
        System.out.println("----------- End method signature: "  + joinPoint.getSignature() + "      Time: " + df.format(new Date()));
    }

    @AfterThrowing(value = "allMethods()", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        System.out.println("================  Exception caught: " + joinPoint.getSignature() + " -> " + e);
    }

}
