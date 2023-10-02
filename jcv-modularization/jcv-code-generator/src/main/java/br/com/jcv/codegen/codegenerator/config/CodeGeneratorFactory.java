package br.com.jcv.codegen.codegenerator.config;

import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAbstractAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserCPF;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorApiControllerAdvice;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBusinessService;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorCommoditiesBaseException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericResponse;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericStatusEnum;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorInvalidFormatException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMainStream;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorIAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorLogback;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemResponse;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorNotFoundException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRegexConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRequestFilter;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorSwaggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CodeGeneratorFactory {

    @Bean("CodeGeneratorSwaggerConfigInstance")
    public ICodeGeneratorIndividual CodeGeneratorSwaggerConfigInstance() {
        log.info("CodeGeneratorSwaggerConfigInstance :: has started successfully");
        return new CodeGeneratorSwaggerConfig();
    }
    @Bean("CodeGeneratorGenericStatusEnumInstance")
    public ICodeGeneratorIndividual CodeGeneratorGenericStatusEnumInstance() {
        log.info("CodeGeneratorGenericStatusEnumInstance :: has started successfully");
        return new CodeGeneratorGenericStatusEnum();
    }
    @Bean("CodeGeneratorGenericResponseInstance")
    public ICodeGeneratorIndividual CodeGeneratorGenericResponseInstance() {
        log.info("CodeGeneratorGenericResponseInstance :: has started successfully");
        return new CodeGeneratorGenericResponse();
    }
    @Bean("CodeGeneratorApiAdviceInstance")
    public ICodeGeneratorIndividual CodeGeneratorApiAdviceInstance() {
        log.info("CodeGeneratorApiAdviceInstance :: has started successfully");
        return new CodeGeneratorApiControllerAdvice();
    }
    @Bean("CodeGeneratorGenericConstantesInstance")
    public ICodeGeneratorIndividual CodeGeneratorGenericConstantesInstance() {
        log.info("CodeGeneratorGenericConstantesInstance :: has started successfully");
        return new CodeGeneratorGenericConstantes();
    }
    @Bean("CodeGeneratorRegexConstantesInstance")
    public ICodeGeneratorIndividual CodeGeneratorRegexConstantesInstance() {
        log.info("CodeGeneratorRegexConstantesInstance :: has started successfully");
        return new CodeGeneratorRegexConstantes();
    }
    @Bean("CodeGeneratorAnalyserCpfInstance")
    public ICodeGeneratorIndividual CodeGeneratorAnalyserCpfInstance() {
        log.info("CodeGeneratorAnalyserCpfInstance :: has started successfully");
        return new CodeGeneratorAnalyserCPF();
    }
    @Bean("CodeGeneratorAbstractAnalyserInstance")
    public ICodeGeneratorIndividual CodeGeneratorAbstractAnalyserInstance() {
        log.info("CodeGeneratorAbstractAnalyserInstance :: has started successfully");
        return new CodeGeneratorAbstractAnalyser();
    }
    @Bean("CodeGeneratorMensagemConstantesInstance")
    public ICodeGeneratorIndividual CodeGeneratorMensagemConstantesInstance() {
        log.info("CodeGeneratorMensagemConstantesInstance :: has started successfully");
        return new CodeGeneratorMensagemConstantes();
    }
    @Bean("CodeGeneratorMensagemResponseInstance")
    public ICodeGeneratorIndividual CodeGeneratorCodeGeneratorMensagemResponseInstanceInstance() {
        log.info("CodeGeneratorMensagemResponseInstance :: has started successfully");
        return new CodeGeneratorMensagemResponse();
    }
    @Bean("CodeGeneratorCommoditiesBaseExceptionInstance")
    public ICodeGeneratorIndividual CodeGeneratorCommoditiesBaseExceptionInstance() {
        log.info("CodeGeneratorCommoditiesBaseExceptionInstance :: has started successfully");
        return new CodeGeneratorCommoditiesBaseException();
    }
    @Bean("CodeGeneratorAnalyserExceptionInstance")
    public ICodeGeneratorIndividual CodeGeneratorAnalyserExceptionInstance() {
        log.info("CodeGeneratorAnalyserExceptionInstance :: has started successfully");
        return new CodeGeneratorAnalyserException();
    }
    @Bean("CodeGeneratorInvalidFormatExceptionInstance")
    public ICodeGeneratorIndividual CodeGeneratorInvalidFormatExceptionInstance() {
        log.info("CodeGeneratorInvalidFormatExceptionInstance :: has started successfully");
        return new CodeGeneratorInvalidFormatException();
    }
    @Bean("CodeGeneratorNotFoundExceptionInstance")
    public ICodeGeneratorIndividual CodeGeneratorNotFoundExceptionInstance() {
        log.info("CodeGeneratorNotFoundExceptionInstance :: has started successfully");
        return new CodeGeneratorNotFoundException();
    }
    @Bean("CodeGeneratorIAnalyserInstance")
    public ICodeGeneratorIndividual CodeGeneratorIAnalyserInstance() {
        log.info("CodeGeneratorIAnalyserInstance :: has started successfully");
        return new CodeGeneratorIAnalyser();
    }
    @Bean("CodeGeneratorBusinessServiceInstance")
    public ICodeGeneratorIndividual CodeGeneratorBusinessServiceInstance() {
        log.info("CodeGeneratorBusinessService :: has started successfully");
        return new CodeGeneratorBusinessService();
    }
    @Bean("CodeGeneratorRequestFilterInstance")
    public ICodeGeneratorIndividual CodeGeneratorRequestFilterInstance() {
        log.info("CodeGeneratorRequestFilterInstance :: has started successfully");
        return new CodeGeneratorRequestFilter();
    }
    @Bean("CodeGeneratorMainStreamInstance")
    public ICodeGeneratorBatch CodeGeneratorMainStreamInstance() {
        log.info("CodeGeneratorMainStreamInstance :: has started successfully");
        return new CodeGeneratorMainStream();
    }
    @Bean("CodeGeneratorLogbackInstance")
    public ICodeGeneratorIndividual CodeGeneratorLogbackInstance() {
        log.info("CodeGeneratorLogbackInstance :: has started successfully");
        return new CodeGeneratorLogback();
    }
}
