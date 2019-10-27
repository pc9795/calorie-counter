package service.calorie.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import service.calorie.util.Constants;
import service.calorie.util.Utils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 14:55
 * Purpose: TODO:
 **/
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handle404(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_NOT_FOUND,
                Constants.ErrorMsg.RESOURCE_NOT_FOUND, response);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public void handleUserAlreadyExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                Constants.ErrorMsg.USER_ALREADY_EXISTS, response);
    }

    @ExceptionHandler({InvalidDataException.class, ValidationException.class})
    public void handleInvalidDataException(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                exc.getMessage(), response);
    }

    @ExceptionHandler(ResourceNotExistException.class)
    public void handleUserNotExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                Constants.ErrorMsg.RESOURCE_NOT_EXIST, response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle400(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                exc.getMessage(), response);
    }

    public void handleForbiddenResourceException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_FORBIDDEN, Constants.ErrorMsg.FORBIDDEN_RESOURCE, response);
    }

    private static void createJSONErrorResponse(int errorCode, String errorMessage, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(errorCode);
        response.getWriter().write(Utils.createErrorJSON(errorCode, errorMessage));
    }
}
