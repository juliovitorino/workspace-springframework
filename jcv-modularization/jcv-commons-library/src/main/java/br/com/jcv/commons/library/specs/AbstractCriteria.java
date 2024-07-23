package br.com.jcv.commons.library.specs;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCriteria {

    public abstract <T> Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder,
            Map<String, Join<Object, Object>> joinMap);

    public <T> Predicate getPredicate(
            CriteriaClause<T> criteriaClause,
            CriteriaBuilder criteriaBuilder,
            Path expression) {

        Predicate predicate = null;
        switch (criteriaClause.getOperator()) {
            case EQUAL_TO:
                predicate = criteriaBuilder.equal(expression, criteriaClause.getValue());
                break;
            case LIKE:
                predicate = criteriaBuilder.like(expression, "%" + criteriaClause.getValue() + "%");
                break;
            case STARTS_WITH:
                predicate = criteriaBuilder.like(expression, criteriaClause.getValue() + "%");
                break;
            case IN:
                predicate = criteriaBuilder.in(expression).value(criteriaClause.getValue());
                break;
            case GT:
                predicate = criteriaBuilder.greaterThan(expression, (Comparable) criteriaClause.getValue());
                break;
            case LT:
                predicate = criteriaBuilder.lessThan(expression, (Comparable) criteriaClause.getValue());
                break;
            case GTE:
                predicate = criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) criteriaClause.getValue());
                break;
            case LTE:
                predicate = criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) criteriaClause.getValue());
                break;
            case NOT_EQUAL:
                predicate = criteriaBuilder.notEqual(expression, criteriaClause.getValue());
                break;
            case IS_NULL:
                predicate = criteriaBuilder.isNull(expression);
                break;
            case NOT_NULL:
                predicate = criteriaBuilder.isNotNull(expression);
                break;
            default:
                log.error("Invalid Operator");
                throw new IllegalArgumentException(criteriaClause.getOperator() + " is not valid operator");
        }
        return predicate;
    }

    public <T> Predicate getPredicateFromFilter(
            CriteriaClause<?> criteriaClause,
            Root<T> root,
            CriteriaBuilder criteriaBuilder,
            Map<String, Join<Object, Object>> attributeToJoin) {

        Assert.notNull(criteriaClause,"Filter must not be null");
        if (attributeToJoin != null && attributeToJoin.get(criteriaClause.getEntityName()) != null) {
            return  getPredicate(criteriaClause, criteriaBuilder, attributeToJoin.get(criteriaClause.getEntityName()).get(criteriaClause.getField()));
        } else {
            return getPredicate(criteriaClause, criteriaBuilder, root.get(criteriaClause.getField()));
        }
    }
}