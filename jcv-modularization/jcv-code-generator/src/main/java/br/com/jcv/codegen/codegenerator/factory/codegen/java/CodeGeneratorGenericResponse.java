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
public class CodeGeneratorGenericResponse extends AbstractCodeGenerator implements ICodeGeneratorIndividual {

    private static final String TEMPLATE = "static/generic-response.template";
    @Override
    public <Input> WritableCode generate(Class<Input> inputClassModel) {
        StringBuffer sbCode = new StringBuffer();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO codegen = prepareCodeGeneratorFromModel(inputClassModel);
        readTemplate(TEMPLATE, sbCode, codegen);
//        writeCode(sbCode,codegen, "/dto/GenericErrorResponse","java");
        TargetFileCodeInfo targetFileCodeInfo = new TargetFileCodeInfo(
                TargetFileEnum.fromCodeGeneratorClass(fullClassNameToSingle(this.getClass().getName())).getTargetFilePath(),
                TARGET_EXTENSION_JAVA);

        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return new WritableCode(sbCode, codegen, targetFileCodeInfo);
    }

}


