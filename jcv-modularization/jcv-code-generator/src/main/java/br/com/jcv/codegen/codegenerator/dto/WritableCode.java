package br.com.jcv.codegen.codegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.apache.bcel.classfile.Code;

@Getter
@Setter
@AllArgsConstructor
public class WritableCode {
    private StringBuffer sourceCode;
    private CodeGeneratorDTO codeGenerator;
    private TargetFileCodeInfo targetFileCodeInfo;
}
