package service.calorie.utils;

import service.calorie.exceptions.InvalidDataException;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 15:59
 * Purpose: Grouping class for operands and operator.
 **/
public class SearchCriteria {
    private String key;
    private SearchOption operation;
    private Object value;

    public SearchCriteria(String key, Object value, String operation) throws InvalidDataException {
        this.key = key;
        this.operation = SpecificationUtils.searchOptionFromStr(operation);
        this.value = value;
    }

    String getKey() {
        return key;
    }

    SearchOption getOperation() {
        return operation;
    }

    Object getValue() {
        return value;
    }

}
