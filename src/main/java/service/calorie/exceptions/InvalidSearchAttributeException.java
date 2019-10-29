package service.calorie.exceptions;

/**
 * Created By: Prashant Chaubey
 * Created On: 29-10-2019 01:32
 * Purpose: Exception when un mapped attribute is used in search query. Can't make it a checked exception as it
 * will violate the Specification mechanisom of JPA.
 **/
public class InvalidSearchAttributeException extends RuntimeException {

    public InvalidSearchAttributeException(String message) {
        super(message);
    }
}
