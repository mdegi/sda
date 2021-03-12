package mt.com.go.deploymentmanagement.aspects;

import mt.com.go.ril.error.exception.go.GoRestTemplateException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.web.client.HttpStatusCodeException;

public class DeploymentManagementErrorConverterAspect {

    @Around("execution(* mt.com.go.deploymentmanagement.client.DeploymentManagementClient.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (HttpStatusCodeException ex) {
            throw new GoRestTemplateException(ex.getStatusCode().value(), ex.getResponseBodyAsString());
        }
    }
}


