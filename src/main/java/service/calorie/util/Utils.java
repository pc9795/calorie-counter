package service.calorie.util;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.Iterator;

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

    //todo check for deletion
    public static String joinCollection(Collection collection) {
        if (collection.size() == 0) {
            return "";
        }
        int i = 0;
        int size = collection.size();
        StringBuilder sb = new StringBuilder();
        Iterator iterator = collection.iterator();
        while (i++ < size - 1) {
            sb.append(iterator.next()).append(", ");
        }
        sb.append(iterator.next());
        return sb.toString();
    }
}
