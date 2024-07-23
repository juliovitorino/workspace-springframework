package br.com.jcv.commons.library.specs;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SimpleCriteria<T> extends AbstractCriteria {

    private final CriteriaClause<T> criteriaClause;

    public SimpleCriteria(CriteriaClause<T> criteriaClause) {
        this.criteriaClause = criteriaClause;
    }

    @Override
    public <X> Predicate toPredicate(
            Root<X> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder,
            Map<String, Join<Object, Object>> joinMap) {
        return getPredicateFromFilter(criteriaClause,root,criteriaBuilder, joinMap);
    }
}