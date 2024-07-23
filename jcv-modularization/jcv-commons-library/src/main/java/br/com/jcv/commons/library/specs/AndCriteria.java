package br.com.jcv.commons.library.specs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AndCriteria extends AbstractCriteria {

    private final List<AbstractCriteria> criteriaList;

    public AndCriteria(List<AbstractCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public <T> Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder,
            Map<String, Join<Object, Object>> joinMap) {
        return criteriaBuilder.and(
                criteriaList.stream()
                        .map(filter -> filter.toPredicate(root,query,criteriaBuilder, joinMap))
                        .collect(Collectors.toList())
                        .toArray(Predicate[]::new));
    }
}