package mt.com.go.deploymentsmanagement.aspects;

import mt.com.go.ril.auth.token.GoToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class DeploymentsManagementAspect {

    /**
     * Format date - Days / Months / 4 digit year / hours / minutes / seconds / milli seconds / nano seconds
     */
    public DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS:SSSS");

    String retrieveClientId(HttpServletRequest request){
        GoToken token = (GoToken) request.getUserPrincipal();
        return token.getClientId();
    }

}
