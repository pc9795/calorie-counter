package service.calorie.util;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
}
