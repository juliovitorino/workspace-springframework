package br.com.jcv.codegen.codegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldDescriptor implements Serializable {
    private String fieldName;
    private String fieldTableName;
    private String fieldType;
    private String fieldReferenceInDto;
    private String fieldDescription;
    private String regexValidation;
    private boolean isPrimaryKey = false;
    private boolean unique = false;
    private boolean nullable = true;
    private boolean insertable = true;
    private boolean updatable = true;
    private String columnDefinition = "";
    private String table = "";
    private int length = 255;
    private int precision = 0;
    private int scale = 0;

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

}
