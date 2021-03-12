package mt.com.go.deploymentmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRequest {

    private String transactionReference;
    private String logDescription;
    private String logReason;
    private List<Udf> udfs;

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public String getLogReason() {
        return logReason;
    }

    public void setLogReason(String logReason) {
        this.logReason = logReason;
    }

    public List<Udf> getUdfs() {
        return udfs;
    }

    public void setUdfs(List<Udf> udfs) {
        this.udfs = udfs;
    }

    @Override
    public String toString() {
        String toString = "Transaction Reference: " + transactionReference + ", Log Description: " + logDescription +
                ", Log Reason: " + logReason;

        String udfs = "";

        if (getUdfs() != null) {
            for (Udf udf : getUdfs()) {
                udfs += udf.toString() + " ";
            }
        }

        return String.format("%s. [Udfs: %s]", toString, udfs);
    }


}
