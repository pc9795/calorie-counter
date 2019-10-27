package service.calorie.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 16:15
 * Purpose: TODO:
 **/
public class ApiSpecificationBuilder<T> {
    private List<SearchCriteria> params;

    public ApiSpecificationBuilder(List<SearchCriteria> params) {
        this.params = params;
    }

    public ApiSpecificationBuilder with(String key, String operation, String value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public ApiSpecification<T> build() {
        if (params.size() == 0) {
            return null;
        }
        List<ApiSpecification<T>> specs = params.stream().map(ApiSpecification<T>::new).collect(Collectors.toList());
        ApiSpecification<T> result = specs.get(0);
        for (int i = 1; i < params.size(); i++) {
        }
        return result;
    }
}
