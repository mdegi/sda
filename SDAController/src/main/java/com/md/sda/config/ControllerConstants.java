package com.md.sda.config;

public class ControllerConstants {

    private ControllerConstants() {}

    public static final String DEPLOYMENT_DATE_VAR = "deploymentDate";

    public static final String V1_SERVICE_CONFIG_VARS_MAPPING = "/v1/services/configVars";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_DATE = "/v1/services/deploymentDate:{deploymentDate}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_STATUS = "/v1/services/deploymentStatus:{deploymentStatus}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENT_DURATION_BY_DATE = "/v1/services/deploymentDurationByDate:{deploymentDate}";
    public static final String V1_SERVICE_SYSTEMS_DEPLOYMENT_WITHIN_TIME_RANGE = "/v1/services/deploymentTimeRange:{timeFrom}-{timeTo}";
    public static final String V1_SERVICE_DEPLOYMENTS_BY_SYSTEM = "/v1/services/system:{systemName}";

    /**
     * Date regex format: yyyyMMdd example: 20201201
     */
    public static final String MAPPING_DATE_REGEX = "^20[0-9]{2}((0(1|3|5|7|8)|10|12)(((0|1|2)[1-9])|3[0-1])|(0(4|6|9)|11)(((0|1|2)[1-9])|30)|02((0[1-9])|((1|2)[0-9])))";

    /**
     * Time regex format: 0000 to 2359
     */
    public static final String MAPPING_TIME_REGEX = "((^[0-1][0-9])|^(2[0-3]))((0[1-9])|([1-5][0-9]))";

}
