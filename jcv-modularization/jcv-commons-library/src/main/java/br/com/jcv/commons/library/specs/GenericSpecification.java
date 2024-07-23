package br.com.jcv.commons.library.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification<T> implements Specification<T> {

    private final AbstractCriteria criteriaConcrete;
    private final JoinTableMap<T> joinTableMap;

    public GenericSpecification(AbstractCriteria criteriaConcrete, JoinTableMap<T> joinTableMap) {
        this.criteriaConcrete = criteriaConcrete;
        this.joinTableMap = joinTableMap;
    }


    @Override
   public Predicate toPredicate(
           Root<T> root,
           CriteriaQuery<?> query,
           CriteriaBuilder criteriaBuilder) {

      if (joinTableMap != null && criteriaConcrete !=null) {
         return criteriaConcrete.toPredicate(root, query, criteriaBuilder,  joinTableMap.getJoinTableMap(root, query));
      }
      return criteriaBuilder.conjunction();
   }
}