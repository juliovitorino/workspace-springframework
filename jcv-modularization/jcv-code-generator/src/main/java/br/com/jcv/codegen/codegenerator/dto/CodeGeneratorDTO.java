package br.com.jcv.codegen.codegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CodeGeneratorDTO implements Serializable {
    private String outputDir;
    private String basePackage;
    private String basePackageSlash;
    private String project;
    private String fullDescription;
    private String tableName;
    private String schema;
    private String author;
    private String baseClass;
    List<FieldDescriptor> fieldDescriptorList;

    public String getBasePackageSlash() {
        return basePackage.replaceAll("\\.","/");
    }

}
