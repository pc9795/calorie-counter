package service.calorie.util;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 16:14
 * Purpose: TODO:
 **/
public class ApiSpecification<T> implements Specification<T> {
    private SearchCriteria searchCriteria;

    public ApiSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (searchCriteria.getOperation()) {
            case "eq":
                return criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()
                );
            case "ge":
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()
                );
            case "gt":
                return criteriaBuilder.greaterThan(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()
                );
            case "le":
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()
                );
            case "lt":
                return criteriaBuilder.lessThan(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()
                );
        }
        return null;
    }
}
