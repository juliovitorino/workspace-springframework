package br.com.jcv.commons.library.specs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaClause<T> {

   /* Name of the variable from Entity class on which filter has to be applied */
   private final String field;
   
   /* Filter operator */
   private final EnumCriteriaOperator operator;
    
   /* Filter value */
   private final T value;
    
   /* Join identifier */
   private final String entityName;

    public CriteriaClause(String field, EnumCriteriaOperator operator, T value, String entityName) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.entityName = entityName;
    }

}