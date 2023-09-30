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
    public String name;
    public String dtoFieldReference;
    public String fieldDescription;
    public String fieldType;
}
