package service.calorie.service;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import service.calorie.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 02:51
 * Purpose: TODO:
 **/
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Custom exception handling for authentication failure.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(Utils.createErrorJSON(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized"));
    }
}
