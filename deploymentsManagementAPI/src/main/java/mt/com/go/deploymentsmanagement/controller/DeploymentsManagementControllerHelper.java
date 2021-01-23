package mt.com.go.deploymentsmanagement.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@NoArgsConstructor
public class DeploymentsManagementControllerHelper {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public Date getDate(String dateParam) {
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(dateParam);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

}
