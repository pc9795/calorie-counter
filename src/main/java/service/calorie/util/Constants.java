package service.calorie.util;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 01:07
 * Purpose: TODO:
 **/
public final class Constants {
    private Constants() {
    }

    public class ErrorMsg {
        public static final String RESOURCE_NOT_FOUND = "Resource not found";
        public static final String USER_ALREADY_EXISTS = "User with this username already exists";
        public static final String BAD_CREDENTIALS = "Bad Credentials";
        public static final String RESOURCE_NOT_EXIST = "Resource not exists";
        public static final String UNAUTHORIZED = "Unauthorized";
        public static final String FORBIDDEN_RESOURCE = "Forbidden";
    }

    public class ExcMsg {

    }

    public class ApiV1Resource {
        public static final String PREFIX = "/api/v1";
        public static final String USER = PREFIX + "/users";
        public static final String MEAL = PREFIX + "/meals";
    }

}
