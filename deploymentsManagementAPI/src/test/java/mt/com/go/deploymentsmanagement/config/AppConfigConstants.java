package mt.com.go.deploymentsmanagement.config;

public class AppConfigConstants {

    public final static String FILE_SYSTEM_PATH = "/depExports";

    public final static String FILE_NAME_REGEX = "^20[0-9]{2}((0(1|3|5|7|8)|10|12)(((0|1|2)[1-9])|3[0-1])|(0(4|6|9)|11)(((0|1|2)[1-9])|30)|02((0[1-9])|((1|2)[0-9])))-export";

    public final static String FILE_EXTENSION = "csv";

    public final static int FILE_SCAN_FIXED_RATE_MILLISECONDS = 4000;

    public final static int FILE_SCAN_INITIAL_DELAY_MILLISECONDS = 1000;

    public final static String DB_URL = "mongodb://localhost:27017";

    public final static String DB_NAME = "deployments";

}
