package br.com.jcv.codegen.codegenerator.factory.codegen.java;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.factory.codegen.AbstractCodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired @Qualifier("CodeGeneratorAnalyserCpfInstance") ICodeGeneratorIndividual generatorAnalyserCpf;
    @Autowired @Qualifier("CodeGeneratorAbstractAnalyserInstance") ICodeGeneratorIndividual generatorAbstractAnalyser;
    @Autowired @Qualifier("CodeGeneratorMensagemConstantesInstance") ICodeGeneratorIndividual generatorMensagemConstantes;
    @Autowired @Qualifier("CodeGeneratorMensagemResponseInstance") ICodeGeneratorIndividual generatorMensagemResponse;
    @Autowired @Qualifier("CodeGeneratorCommoditiesBaseExceptionInstance") ICodeGeneratorIndividual generatorCommoditiesBaseException;
    @Autowired @Qualifier("CodeGeneratorAnalyserExceptionInstance") ICodeGeneratorIndividual generatorAnalyserException;
    @Autowired @Qualifier("CodeGeneratorNotFoundExceptionInstance") ICodeGeneratorIndividual generatorNotFoundException;
    @Autowired @Qualifier("CodeGeneratorInvalidFormatExceptionInstance") ICodeGeneratorIndividual generatorInvalidFormatException;
    @Autowired @Qualifier("CodeGeneratorIAnalyserInstance") ICodeGeneratorIndividual generatorIAnalyser;
    @Autowired @Qualifier("CodeGeneratorBusinessServiceInstance") ICodeGeneratorIndividual generatorBusinessService;
    @Autowired @Qualifier("CodeGeneratorRequestFilterInstance") ICodeGeneratorIndividual generatorRequestFilter;
    @Autowired @Qualifier("CodeGeneratorLogbackInstance") ICodeGeneratorIndividual generatorLogabck;
    @Autowired @Qualifier("CodeGeneratorRegexConstantesInstance") ICodeGeneratorIndividual generatorRegexConstantes;
    @Autowired @Qualifier("CodeGeneratorGenericConstantesInstance") ICodeGeneratorIndividual generatorGenericConstantes;
    @Autowired @Qualifier("CodeGeneratorApiAdviceInstance") ICodeGeneratorIndividual generatorApiAdvice;
    @Autowired @Qualifier("CodeGeneratorGenericResponseInstance") ICodeGeneratorIndividual generatorGenericResponse;
    @Autowired @Qualifier("CodeGeneratorGenericStatusEnumInstance") ICodeGeneratorIndividual generatorGenericStatusEnum;
    @Autowired @Qualifier("CodeGeneratorSwaggerConfigInstance") ICodeGeneratorIndividual generatorSwaggerConfig;
    @Autowired @Qualifier("CodeGeneratorConstantesInstance") ICodeGeneratorIndividual generatorConstantes;
    @Autowired @Qualifier("CodeGeneratorDtoPadraoInstance") ICodeGeneratorIndividual generatorDtoPadrao;
    @Autowired @Qualifier("CodeGeneratorDtoInstance") ICodeGeneratorIndividual generatorDto;
    @Autowired @Qualifier("CodeGeneratorRepositoryInstance") ICodeGeneratorIndividual generatorRepository;
    @Autowired @Qualifier("CodeGeneratorCommoditieServiceInstance") ICodeGeneratorIndividual generatorCommoditieService;
    @Autowired @Qualifier("CodeGeneratorServiceInstance") ICodeGeneratorIndividual generatorService;
    @Autowired @Qualifier("CodeGeneratorServiceImplInstance") ICodeGeneratorIndividual generatorServiceImpl;
    @Autowired @Qualifier("CodeGeneratorControllerInstance") ICodeGeneratorIndividual generatorController;

    @Override
    public <Input> List<WritableCode> generate(Class<Input> inputClassModel) {
        List<WritableCode> codeInBatch = new ArrayList<>();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());
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
        String OutputFilename = codegen.getOutputDir() + filename + "." + extension;
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
