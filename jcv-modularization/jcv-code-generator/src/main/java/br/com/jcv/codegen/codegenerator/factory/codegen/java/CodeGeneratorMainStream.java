package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CodeGeneratorMainStream extends AbstractCodeGenerator implements ICodeGeneratorBatch {

    @Autowired private  @Qualifier("CodeGeneratorBuilderInstance") ICodeGeneratorIndividual generatorBuilder;
    @Autowired private  @Qualifier("CodeGeneratorAnalyserCpfInstance") ICodeGeneratorIndividual generatorAnalyserCpf;
    @Autowired private  @Qualifier("CodeGeneratorAbstractAnalyserInstance") ICodeGeneratorIndividual generatorAbstractAnalyser;
    @Autowired private  @Qualifier("CodeGeneratorMensagemConstantesInstance") ICodeGeneratorIndividual generatorMensagemConstantes;
    @Autowired private  @Qualifier("CodeGeneratorMensagemResponseInstance") ICodeGeneratorIndividual generatorMensagemResponse;
    @Autowired private  @Qualifier("CodeGeneratorCommoditiesBaseExceptionInstance") ICodeGeneratorIndividual generatorCommoditiesBaseException;
    @Autowired private  @Qualifier("CodeGeneratorAnalyserExceptionInstance") ICodeGeneratorIndividual generatorAnalyserException;
    @Autowired private  @Qualifier("CodeGeneratorNotFoundExceptionInstance") ICodeGeneratorIndividual generatorNotFoundException;
    @Autowired private  @Qualifier("CodeGeneratorInvalidFormatExceptionInstance") ICodeGeneratorIndividual generatorInvalidFormatException;
    @Autowired private  @Qualifier("CodeGeneratorIAnalyserInstance") ICodeGeneratorIndividual generatorIAnalyser;
    @Autowired private  @Qualifier("CodeGeneratorBusinessServiceInstance") ICodeGeneratorIndividual generatorBusinessService;
    @Autowired private  @Qualifier("CodeGeneratorRequestFilterInstance") ICodeGeneratorIndividual generatorRequestFilter;
    @Autowired private  @Qualifier("CodeGeneratorLogbackInstance") ICodeGeneratorIndividual generatorLogabck;
    @Autowired private  @Qualifier("CodeGeneratorRegexConstantesInstance") ICodeGeneratorIndividual generatorRegexConstantes;
    @Autowired private  @Qualifier("CodeGeneratorGenericConstantesInstance") ICodeGeneratorIndividual generatorGenericConstantes;
    @Autowired private  @Qualifier("CodeGeneratorApiAdviceInstance") ICodeGeneratorIndividual generatorApiAdvice;
    @Autowired private  @Qualifier("CodeGeneratorGenericResponseInstance") ICodeGeneratorIndividual generatorGenericResponse;
    @Autowired private  @Qualifier("CodeGeneratorGenericStatusEnumInstance") ICodeGeneratorIndividual generatorGenericStatusEnum;
    @Autowired private  @Qualifier("CodeGeneratorSwaggerConfigInstance") ICodeGeneratorIndividual generatorSwaggerConfig;
    @Autowired private  @Qualifier("CodeGeneratorConstantesInstance") ICodeGeneratorIndividual generatorConstantes;
    @Autowired private  @Qualifier("CodeGeneratorDtoPadraoInstance") ICodeGeneratorIndividual generatorDtoPadrao;
    @Autowired private  @Qualifier("CodeGeneratorDtoInstance") ICodeGeneratorIndividual generatorDto;
    @Autowired private  @Qualifier("CodeGeneratorRepositoryInstance") ICodeGeneratorIndividual generatorRepository;
    @Autowired private  @Qualifier("CodeGeneratorCommoditieServiceInstance") ICodeGeneratorIndividual generatorCommoditieService;
    @Autowired private  @Qualifier("CodeGeneratorServiceInstance") ICodeGeneratorIndividual generatorService;
    @Autowired private  @Qualifier("CodeGeneratorServiceImplInstance") ICodeGeneratorIndividual generatorServiceImpl;
    @Autowired private  @Qualifier("CodeGeneratorControllerInstance") ICodeGeneratorIndividual generatorController;

    @Override
    public <Input> List<WritableCode> generate(Class<Input> inputClassModel) {
        List<WritableCode> codeInBatch = new ArrayList<>();
        log.info("generate :: is reading {} attributes", inputClassModel.hashCode());
        codeInBatch.add(generatorBusinessService.generate(inputClassModel));
        codeInBatch.add(generatorRequestFilter.generate(inputClassModel));
        codeInBatch.add(generatorLogabck.generate(inputClassModel));
        codeInBatch.add(generatorIAnalyser.generate(inputClassModel));
        codeInBatch.add(generatorAnalyserException.generate(inputClassModel));
        codeInBatch.add(generatorNotFoundException.generate(inputClassModel));
        codeInBatch.add(generatorInvalidFormatException.generate(inputClassModel));
        codeInBatch.add(generatorCommoditiesBaseException.generate(inputClassModel));
        codeInBatch.add(generatorMensagemResponse.generate(inputClassModel));
        codeInBatch.add(generatorMensagemConstantes.generate(inputClassModel));
        codeInBatch.add(generatorAbstractAnalyser.generate(inputClassModel));
        codeInBatch.add(generatorAnalyserCpf.generate(inputClassModel));
        codeInBatch.add(generatorRegexConstantes.generate(inputClassModel));
        codeInBatch.add(generatorGenericConstantes.generate(inputClassModel));
        codeInBatch.add(generatorApiAdvice.generate(inputClassModel));
        codeInBatch.add(generatorGenericResponse.generate(inputClassModel));
        codeInBatch.add(generatorGenericStatusEnum.generate(inputClassModel));
        codeInBatch.add(generatorConstantes.generate(inputClassModel));
        codeInBatch.add(generatorDtoPadrao.generate(inputClassModel));
        codeInBatch.add(generatorDto.generate(inputClassModel));
        codeInBatch.add(generatorCommoditieService.generate(inputClassModel));
        codeInBatch.add(generatorService.generate(inputClassModel));
        codeInBatch.add(generatorRepository.generate(inputClassModel));
        codeInBatch.add(generatorServiceImpl.generate(inputClassModel));
        codeInBatch.add(generatorSwaggerConfig.generate(inputClassModel));
        codeInBatch.add(generatorController.generate(inputClassModel));
        codeInBatch.add(generatorBuilder.generate(inputClassModel));
        log.info("generate :: has been executed");
        return codeInBatch;
    }

    @Override
    public void flushCode(List<WritableCode> codes) {
        for(WritableCode writableCode: codes) {
            log.info("flushCode :: is flushing source code -> {}.{}",
                    writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                    writableCode.getTargetFileCodeInfo().getTargetExtension());

            //TODO Antes de gravar o codigo fonte verificar um flag se o dono quer sobrescrever o código existente

            writeCode(writableCode.getSourceCode(),
                    writableCode.getCodeGenerator(),
                    writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                    writableCode.getTargetFileCodeInfo().getTargetExtension());
        }

    }

    private void writeCode(StringBuffer code, CodeGeneratorDTO codegen, String filename, String extension){

        String OutputFilename = codegen.getOutputDir() + "/" + codegen.getBasePackageSlash() +filename + "." + extension;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(OutputFilename);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.write(code.toString().getBytes(StandardCharsets.UTF_8));
            outStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
