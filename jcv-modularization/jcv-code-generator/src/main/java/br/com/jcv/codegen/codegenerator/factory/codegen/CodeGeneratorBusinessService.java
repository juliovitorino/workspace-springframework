package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("CodeGeneratorBusinessService")
@Slf4j
public class CodeGeneratorBusinessService extends AbstractCodeGenerator implements ICodeGenerator {
    @Override
    public <Input> StringBuilder generate(Class<Input> inputClassModel) {
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO<Input> codegen = prepareCodeGeneratorFromModel(inputClassModel);


        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return null;
    }
}
