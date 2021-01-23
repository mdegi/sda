package mt.com.go.deploymentsmanagement.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class AuthenticationFilter implements Filter {

    /**
     * Setting a constant here for proof of concept.
     * Such details should be stored in some external config file
     */
    private final String PUBLIC_KEY = "public_key";
    private final String PUBLIC_KEY_VALUE = "123456";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean authorised = false;

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equals(PUBLIC_KEY) && httpServletRequest.getHeader(PUBLIC_KEY).equals(PUBLIC_KEY_VALUE)) {
                authorised = true;
                break;
            }
        }

        if (!authorised) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
