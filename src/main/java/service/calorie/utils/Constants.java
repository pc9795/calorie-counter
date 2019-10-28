package service.calorie.utils;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 01:07
 * Purpose: Constants for the project.
 **/
public final class Constants {
    private Constants() {
    }

    /**
     * Error messages for error codes.
     */
    public class ErrorMsg {
        public static final String RESOURCE_NOT_FOUND = "Resource not found";
        public static final String USER_ALREADY_EXISTS = "User with this username already exists";
        public static final String BAD_CREDENTIALS = "Bad Credentials";
        public static final String UNAUTHORIZED = "Unauthorized";
        public static final String FORBIDDEN_RESOURCE = "Forbidden Resource";
    }

    /**
     * Resource urls.
     */
    public class ApiV1Resource {
        private static final String PREFIX = "/api/v1";
        public static final String USER = PREFIX + "/users";
        public static final String MEAL = PREFIX + "/meals";
    }

    // Swagger will search this package for API documentation.
    public static String SWAGGER_BASE_PACKAGE = "service.calorie.api.v1";
}
