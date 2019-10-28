package service.calorie.util;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 12:18
 * Purpose: TODO:
 **/
public final class Utils {
    private Utils() {
    }

    public static String createErrorJSON(int errorCode, String errorMessage) {
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("code", errorCode);
        errorNode.put("message", errorMessage);
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.set("error", errorNode);
        return root.toString();
    }

    /**
     * Utility method to create a JSON response for a particular error code and message.
     *
     * @param errorCode
     * @param errorMessage
     * @param response
     * @throws IOException
     */
    public static void createJSONErrorResponse(int errorCode, String errorMessage, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(errorCode);
        response.getWriter().write(Utils.createErrorJSON(errorCode, errorMessage));
    }
}
