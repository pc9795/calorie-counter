package service.calorie.util;

import service.calorie.exceptions.InvalidSearchQueryException;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 15:59
 * Purpose: TODO:
 **/
public class SearchCriteria {
    private String key;
    private SearchOption operation;
    private Object value;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, Object value, String operation) throws InvalidSearchQueryException {
        this.key = key;
        this.operation = SpecificationUtils.searchOptionFromStr(operation);
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SearchOption getOperation() {
        return operation;
    }

    public void setOperation(SearchOption operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
