package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CodeGeneratorAnalyserCPF extends AbstractCodeGenerator implements ICodeGenerator {

    private static final String TEMPLATE = "static/analyser-cpf.template";
    @Override
    public <Input> StringBuffer generate(Class<Input> inputClassModel) {
        StringBuffer sbCode = new StringBuffer();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO codegen = prepareCodeGeneratorFromModel(inputClassModel);
        readTemplate(TEMPLATE, sbCode, codegen);
        writeCode(sbCode,codegen, "/analyser/AnalyserCPF","java");

        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return sbCode;
    }

}
