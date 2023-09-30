package br.com.jcv.codegen.codegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CodeGeneratorDTO<Model> implements Serializable {
    private String basePackage;
    private String project;
    private String fullDescription;
    private String tableName;
    private String schema;
    private Class<Model> modelName;
    List<FieldDescriptor> fieldDescriptorList;

}
