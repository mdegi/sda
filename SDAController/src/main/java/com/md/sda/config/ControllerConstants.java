package com.md.sda.config;

public class ControllerConstants {

    private ControllerConstants() {}

    public static final String DEPLOYMENT_VAR = "deployment";

    public static final String V1_SERVICE_CONFIG_VARS_MAPPING = "/v1/services/configVars";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_DATE = "/v1/services/deployment:{deploymentDate}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_STATUS = "/v1/services/deploymentStatus:{deploymentStatus}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENT_DURATION_BY_DATE = "/v1/services/deploymentDuration:{deploymentDate}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENT_WITHIN_TIME_RANGE = "/v1/services/deploymentTimeFrom:{timeFrom}-{timeTo}";
    public static final String V1_SERVICE_DEPLOYMENTS_BY_SYSTEM = "/v1/services/system:{systemName}";

}
