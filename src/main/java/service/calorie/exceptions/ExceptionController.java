package service.calorie.exceptions;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import service.calorie.util.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 14:55
 * Purpose: TODO:
 **/
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handle404(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        httpServletResponse.getWriter().write(Utils.createErrorJSON(HttpServletResponse.SC_NOT_FOUND,
                "Resource not found"));
    }

    @ExceptionHandler(UserAlreadyExistExcepion.class)
    public void handleUserAlreadyExistException(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        httpServletResponse.getWriter().write(Utils.createErrorJSON(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "User with this username already exists"));
    }
}
