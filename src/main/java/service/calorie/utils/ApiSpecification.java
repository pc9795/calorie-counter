package service.calorie.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 16:14
 * Purpose: Custom specification for JPA criteria builder queries.
 **/
public class ApiSpecification<T> implements Specification<T> {
    private SearchCriteria searchCriteria;

    public ApiSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * Handling different operators.
     *
     * @param root
     * @param query
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (searchCriteria.getOperation()) {
            case EQUALS:
                return criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()), searchCriteria.getValue()
                );
            case GREATER_THAN_OR_EQUAL_TO:
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get(searchCriteria.getKey()), (Comparable) searchCriteria.getValue()
                );
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(
                        root.get(searchCriteria.getKey()), (Comparable) searchCriteria.getValue()
                );
            case LESS_THAN_OR_EQUAL_TO:
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get(searchCriteria.getKey()), (Comparable) searchCriteria.getValue()
                );
            case LESS_THAN:
                return criteriaBuilder.lessThan(
                        root.get(searchCriteria.getKey()), (Comparable) searchCriteria.getValue()
                );
        }
        return null;
    }
}
