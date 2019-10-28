package service.calorie.exceptions;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 17:02
 * Purpose: Exception if the search query is incorrect.
 **/
public class InvalidSearchQueryException extends RuntimeException {

    public InvalidSearchQueryException() {
    }

    public InvalidSearchQueryException(String message) {
        super(message);
    }
}
