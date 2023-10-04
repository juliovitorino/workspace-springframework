package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.TargetFileCodeInfo;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.enums.TargetFileEnum;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CodeGeneratorApiControllerAdvice extends AbstractCodeGenerator implements ICodeGeneratorIndividual {

    private static final String TEMPLATE = "static/api-advice.template";
    @Override
    public <Input> WritableCode generate(Class<Input> inputClassModel) {
        StringBuffer sbCode = new StringBuffer();
        log.info("generate :: is reading {} attributes", inputClassModel.hashCode());

        CodeGeneratorDTO codegen = prepareCodeGeneratorFromModel(inputClassModel);
        readTemplate(TEMPLATE, sbCode, codegen);
        TargetFileCodeInfo targetFileCodeInfo = new TargetFileCodeInfo(
                "/controller/Api" + codegen.getBaseClass() + "ControllerAdvice"
                ,TARGET_EXTENSION_JAVA);

        log.info("generate :: has been executed");
        return new WritableCode(sbCode, codegen, targetFileCodeInfo);
    }

}

