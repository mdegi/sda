package mt.com.go.deploymentsmanagement.aspects;

import mt.com.go.ril.auth.AuthorityHelper;
import mt.com.go.ril.error.Error;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Aspect
@Component
public class AuthorityAspect extends DeploymentsManagementAspect{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityAspect.class);

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.reloadFiles(..))")
    public Object isReloadFilesAuthorised(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("reloadFiles");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.getSystemsDeploymentByPostDeploymentStatus(..))")
    public Object isGetSystemsDeploymentByPostDeploymentStatusAuthorised(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("getSystemsDeploymentByPostDeploymentStatus");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.getDeploymentTotalDurationToDeployByDate(..))")
    public Object isGetDeploymentTotalDurationToDeployByDateAuthorised(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("getDeploymentTotalDurationToDeployByDate");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.getAllSystemDeploymentsWithinDateRange(..))")
    public Object isGetAllSystemDeploymentsWithinDateRangeAuthorised(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("getAllSystemDeploymentsWithinDateRange");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.getAllDeploymentsBySystem(..))")
    public Object isGetDeploymentsBySystemAuthorised(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("getAllDeploymentsBySystem");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    @Around("execution(* mt.com.go.deploymentsmanagement.controller.DeploymentsManagementController.getSystemsDeploymentByDate(..))")
    public Object isGetDeploymentByDateValidRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ResponseEntity<?>> errorResponse = checkOperationAuthorisation("getSystemsDeploymentByDate");
        return errorResponse.isEmpty() ? joinPoint.proceed() : errorResponse.get();
    }

    private Optional<ResponseEntity<?>> checkOperationAuthorisation(String permission) {
        if (permission == null || !AuthorityHelper.hasAuthority(permission)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            LOGGER.error(String.format("Query by Client: %s failed because permission %s is missing", retrieveClientId(request), permission));
            return Optional.of(new ResponseEntity<>(
                    Error.createApiError(
                            "insufficient_rights", "You are not allowed to perform this operation"),
                    HttpStatus.UNAUTHORIZED));
        }
        return Optional.empty();
    }

}
