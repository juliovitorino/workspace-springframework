package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.TargetFileCodeInfo;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.enums.CodeGeneratorTags;
import br.com.jcv.codegen.codegenerator.enums.TargetFileEnum;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CodeGeneratorServiceImpl extends AbstractCodeGenerator implements ICodeGeneratorIndividual {

    private static final String TEMPLATE = "static/service-impl.template";
    @Override
    public <Input> WritableCode generate(Class<Input> inputClassModel) {
        StringBuffer sbCode = new StringBuffer();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO codegen = prepareCodeGeneratorFromModel(inputClassModel);
        readTemplate(TEMPLATE, sbCode, codegen);
        String filePath = TargetFileEnum.fromCodeGeneratorClass(fullClassNameToSingle(this.getClass().getName())).getTargetFilePath();
        TargetFileCodeInfo targetFileCodeInfo = new TargetFileCodeInfo(
                filePath.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass()),
                TARGET_EXTENSION_JAVA);

        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return new WritableCode(sbCode, codegen, targetFileCodeInfo);
    }

}


