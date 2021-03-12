package mt.com.go.deploymentmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jurgen.Camilleri on 16/05/2016.
 * A class mapping a key to a value used for consumers to send additional data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Udf {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[Key = %s, Value = %s]", key, value);
    }
}
