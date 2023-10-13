package com.jwick.continental.deathagreement.controller.v1.business.codegen;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CodeGeneratorMainStream extends AbstractCodeGenerator implements ICodeGeneratorBatch {

    @Autowired private  @Qualifier("CodeGeneratorBuilderInstance") ICodeGeneratorIndividual generatorBuilder;
    @Autowired private  @Qualifier("CodeGeneratorNotFoundExceptionInstance") ICodeGeneratorIndividual generatorNotFoundException;
    @Autowired private  @Qualifier("CodeGeneratorLogbackInstance") ICodeGeneratorIndividual generatorLogabck;
    @Autowired private  @Qualifier("CodeGeneratorApiAdviceInstance") ICodeGeneratorIndividual generatorApiAdvice;
    @Autowired private  @Qualifier("CodeGeneratorSwaggerConfigInstance") ICodeGeneratorIndividual generatorSwaggerConfig;
    @Autowired private  @Qualifier("CodeGeneratorDtoInstance") ICodeGeneratorIndividual generatorDto;
    @Autowired private  @Qualifier("CodeGeneratorRepositoryInstance") ICodeGeneratorIndividual generatorRepository;
    @Autowired private  @Qualifier("CodeGeneratorServiceInstance") ICodeGeneratorIndividual generatorService;
    @Autowired private  @Qualifier("CodeGeneratorServiceImplInstance") ICodeGeneratorIndividual generatorServiceImpl;
    @Autowired private  @Qualifier("CodeGeneratorControllerInstance") ICodeGeneratorIndividual generatorController;
    @Autowired private  @Qualifier("CodeGeneratorTansactionJpaConfigInstance") ICodeGeneratorIndividual generatorTransactionJpaConfig;

    @Override
    public <Input> List<WritableCode> generate(Class<Input> inputClassModel) {
        List<WritableCode> codeInBatch = new ArrayList<>();
        log.info("generate :: is reading {} attributes", inputClassModel.hashCode());
        codeInBatch.add(generatorApiAdvice.generate(inputClassModel));
        codeInBatch.add(generatorDto.generate(inputClassModel));
        codeInBatch.add(generatorNotFoundException.generate(inputClassModel));
        codeInBatch.add(generatorLogabck.generate(inputClassModel));
        codeInBatch.add(generatorService.generate(inputClassModel));
        codeInBatch.add(generatorRepository.generate(inputClassModel));
        codeInBatch.add(generatorServiceImpl.generate(inputClassModel));
        codeInBatch.add(generatorSwaggerConfig.generate(inputClassModel));
        codeInBatch.add(generatorController.generate(inputClassModel));
        codeInBatch.add(generatorBuilder.generate(inputClassModel));
        codeInBatch.add(generatorTransactionJpaConfig.generate(inputClassModel));
        log.info("generate :: has been executed");
        return codeInBatch;
    }

    @Override
    public void flushCode(List<WritableCode> codes) {
        for(WritableCode writableCode: codes) {
            log.info("flushCode :: is flushing source code -> {}.{}",
                    writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                    writableCode.getTargetFileCodeInfo().getTargetExtension());

            writeCode(writableCode.getSourceCode(),
                    writableCode.getCodeGenerator(),
                    writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                    writableCode.getTargetFileCodeInfo().getTargetExtension());
        }

    }

    private void writeCode(StringBuffer code, CodeGeneratorDTO codegen, String filename, String extension){

        String OutputFilename = codegen.getOutputDir() + "/" + codegen.getBasePackageSlash() +filename + "." + extension;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(OutputFilename);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.write(code.toString().getBytes(StandardCharsets.UTF_8));
            outStream.close();
        } catch (IOException e){
            throw new CommoditieBaseException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
