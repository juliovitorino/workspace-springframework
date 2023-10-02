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

    @Override
    public <Input> List<WritableCode> generate(Class<Input> inputClassModel) {
        List<WritableCode> codeInBatch = new ArrayList<>();
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        generatorBusinessService.generate(inputClassModel);
        generatorRequestFilter.generate(inputClassModel);
        generatorLogabck.generate(inputClassModel);
        generatorIAnalyser.generate(inputClassModel);
        generatorAnalyserException.generate(inputClassModel);
        generatorCommoditiesBaseException.generate(inputClassModel);
        generatorMensagemResponse.generate(inputClassModel);
        generatorMensagemConstantes.generate(inputClassModel);
        generatorAbstractAnalyser.generate(inputClassModel);
        generatorAnalyserCpf.generate(inputClassModel);
        generatorRegexConstantes.generate(inputClassModel);
        generatorGenericConstantes.generate(inputClassModel);
        generatorApiAdvice.generate(inputClassModel);
        generatorGenericResponse.generate(inputClassModel);
        generatorGenericStatusEnum.generate(inputClassModel);
        //generatorSwaggerConfig.generate(inputClassModel);  // Verificar as dependencias

        log.info("generate :: has been executed");
        return codeInBatch;
    }

    @Override
    public void flushCode(List<WritableCode> codes) {
        for(WritableCode writableCode: codes) {
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
