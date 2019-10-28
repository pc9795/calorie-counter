package service.calorie.exceptions;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import service.calorie.utils.Constants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static service.calorie.utils.Utils.createJSONErrorResponse;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 14:55
 * Purpose: Controller for handling various exceptions.
 **/
@ControllerAdvice
public class ExceptionController {

    /**
     * Resource url is not mapped.
     *
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handle404(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_NOT_FOUND, Constants.ErrorMsg.RESOURCE_NOT_FOUND, response);
    }


    /**
     * User already exists in the database.
     *
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public void handleUserAlreadyExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ErrorMsg.USER_ALREADY_EXISTS,
                response);
    }

    /**
     * If data is not in the correct format. Custom validations.
     *
     * @param exc
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(InvalidDataException.class)
    public void handleInvalidDataException(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, exc.getMessage(), response);
    }

    /**
     * Errors caused by bean validation api.
     *
     * @param exc
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exc,
                                                      HttpServletResponse response) throws IOException {
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, errors.toString(), response);
    }

    /**
     * If the requested resource is not present.
     *
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(ResourceNotExistException.class)
    public void handleResourceNotExistException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, Constants.ErrorMsg.RESOURCE_NOT_FOUND, response);
    }

    /**
     * Custom forbidden resource implementation.
     *
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(ForbiddenResourceException.class)
    public void handleForbiddenResourceException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_FORBIDDEN, Constants.ErrorMsg.FORBIDDEN_RESOURCE, response);
    }

    @ExceptionHandler(Exception.class)
    public void handleAll(Exception exc, HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                Constants.ErrorMsg.INTERNAL_SERVER_ERROR, response);
    }
}
