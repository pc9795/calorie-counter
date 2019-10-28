package service.calorie.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import service.calorie.util.Constants;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;

import static service.calorie.util.Utils.createJSONErrorResponse;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 14:55
 * Purpose: Controller for handling various exceptions.
 **/
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handle404(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_NOT_FOUND, Constants.ErrorMsg.RESOURCE_NOT_FOUND, response);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public void handleUserAlreadyExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ErrorMsg.USER_ALREADY_EXISTS,
                response);
    }

    @ExceptionHandler({InvalidDataException.class, ValidationException.class})
    public void handleInvalidDataException(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, exc.getMessage(), response);
    }

    @ExceptionHandler(ResourceNotExistException.class)
    public void handleUserNotExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, Constants.ErrorMsg.RESOURCE_NOT_FOUND, response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle400(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, exc.getMessage(), response);
    }

    @ExceptionHandler(ForbiddenResourceException.class)
    public void handleForbiddenResourceException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_FORBIDDEN, Constants.ErrorMsg.FORBIDDEN_RESOURCE, response);
    }


}
