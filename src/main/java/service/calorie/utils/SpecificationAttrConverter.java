package service.calorie.utils;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 22:50
 * Purpose: Convert attribute values according to key.
 **/

@FunctionalInterface
public interface SpecificationAttrConverter {

    Object processAttr(String attributeName, Object attributeValue);
}
