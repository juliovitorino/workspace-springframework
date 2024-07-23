package br.com.jcv.commons.library.specs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OrCriteria extends AbstractCriteria {

    private final List<AbstractCriteria> criteriaList;

    public OrCriteria(List<AbstractCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public <T> Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder,
            Map<String, Join<Object, Object>> joinMap) {
        return criteriaBuilder.or(
                criteriaList.stream()
                .map(criteria -> criteria.toPredicate(root, query, criteriaBuilder, joinMap))
                .collect(Collectors.toList())
                .toArray(Predicate[]::new));
    }
}