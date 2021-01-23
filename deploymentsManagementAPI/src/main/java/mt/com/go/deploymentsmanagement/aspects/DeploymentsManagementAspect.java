package mt.com.go.deploymentsmanagement.aspects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class DeploymentsManagementAspect {

    /**
     * Format date - Days / Months / 4 digit year / hours / minutes / seconds / milli seconds / nano seconds
     */
    public DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS:SSSS");

}
