package service.calorie.exceptions;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 16:21
 * Purpose: Exception if passed data is not valid.
 **/
public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message);
    }
}
