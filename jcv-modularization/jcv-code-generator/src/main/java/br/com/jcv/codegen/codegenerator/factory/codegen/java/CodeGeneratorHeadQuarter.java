package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
public class CodeGeneratorHeadQuarter extends AbstractCodeGenerator implements ICodeGenerator {

    @Autowired @Qualifier("CodeGeneratorIAnalyserInstance")ICodeGenerator generatorIAnalyser;
    @Autowired @Qualifier("CodeGeneratorBusinessServiceInstance")ICodeGenerator generatorBusinessService;
    @Autowired @Qualifier("CodeGeneratorRequestFilterInstance") ICodeGenerator generatorRequestFilter;
    @Autowired @Qualifier("CodeGeneratorLogbackInstance") ICodeGenerator generatorLogabck;

    private static final String TEMPLATE = "static/businessService.template";
    @Override
    public <Input> StringBuffer generate(Class<Input> inputClassModel) {
        StringBuffer sbCode = new StringBuffer();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        generatorBusinessService.generate(inputClassModel);
        generatorRequestFilter.generate(inputClassModel);
        generatorLogabck.generate(inputClassModel);
        generatorIAnalyser.generate(inputClassModel);

        log.info("generate :: has been executed");
        return sbCode;
    }

}
