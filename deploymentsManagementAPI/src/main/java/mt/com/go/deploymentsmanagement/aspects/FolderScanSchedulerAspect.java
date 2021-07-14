package mt.com.go.deploymentsmanagement.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class FolderScanSchedulerAspect extends DeploymentsManagementAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderScanSchedulerAspect.class);

    @Pointcut("execution(* mt.com.go.deploymentsmanagement.schedulingTasks.FolderScanScheduler.*(..))")
    private void allMethods() {}

    @Around("allMethods()")
    public void logDurationAdvice(ProceedingJoinPoint joinPoint) {
        LOGGER.info("----------- Starting method signature: "  + joinPoint.getSignature() + " Time: " + df.format(new Date()));
        try {
            joinPoint.proceed();
        } catch (Throwable t) {
            LOGGER.error("Error occurred executing method: " + joinPoint.getSignature() + ": " + t.getMessage());
        }
        LOGGER.info("----------- End method signature: "  + joinPoint.getSignature() + "      Time: " + df.format(new Date()));
    }

    @AfterThrowing(value = "allMethods()", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        LOGGER.error("Exception caught: " + joinPoint.getSignature() + ": " + e);
    }

}
