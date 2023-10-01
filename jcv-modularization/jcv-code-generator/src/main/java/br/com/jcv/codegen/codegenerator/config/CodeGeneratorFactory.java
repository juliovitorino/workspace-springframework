package br.com.jcv.codegen.codegenerator.config;

import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAbstractAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserCPF;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorApiControllerAdvice;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBusinessService;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorCommoditiesBaseException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericResponse;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericStatusEnum;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorHeadQuarter;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorIAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorLogback;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemResponse;
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
    public ICodeGenerator CodeGeneratorSwaggerConfigInstance() {
        log.info("CodeGeneratorSwaggerConfigInstance :: has started successfully");
        return new CodeGeneratorSwaggerConfig();
    }
    @Bean("CodeGeneratorGenericStatusEnumInstance")
    public ICodeGenerator CodeGeneratorGenericStatusEnumInstance() {
        log.info("CodeGeneratorGenericStatusEnumInstance :: has started successfully");
        return new CodeGeneratorGenericStatusEnum();
    }
    @Bean("CodeGeneratorGenericResponseInstance")
    public ICodeGenerator CodeGeneratorGenericResponseInstance() {
        log.info("CodeGeneratorGenericResponseInstance :: has started successfully");
        return new CodeGeneratorGenericResponse();
    }
    @Bean("CodeGeneratorApiAdviceInstance")
    public ICodeGenerator CodeGeneratorApiAdviceInstance() {
        log.info("CodeGeneratorApiAdviceInstance :: has started successfully");
        return new CodeGeneratorApiControllerAdvice();
    }
    @Bean("CodeGeneratorGenericConstantesInstance")
    public ICodeGenerator CodeGeneratorGenericConstantesInstance() {
        log.info("CodeGeneratorGenericConstantesInstance :: has started successfully");
        return new CodeGeneratorGenericConstantes();
    }
    @Bean("CodeGeneratorRegexConstantesInstance")
    public ICodeGenerator CodeGeneratorRegexConstantesInstance() {
        log.info("CodeGeneratorRegexConstantesInstance :: has started successfully");
        return new CodeGeneratorRegexConstantes();
    }
    @Bean("CodeGeneratorAnalyserCpfInstance")
    public ICodeGenerator CodeGeneratorAnalyserCpfInstance() {
        log.info("CodeGeneratorAnalyserCpfInstance :: has started successfully");
        return new CodeGeneratorAnalyserCPF();
    }
    @Bean("CodeGeneratorAbstractAnalyserInstance")
    public ICodeGenerator CodeGeneratorAbstractAnalyserInstance() {
        log.info("CodeGeneratorAbstractAnalyserInstance :: has started successfully");
        return new CodeGeneratorAbstractAnalyser();
    }
    @Bean("CodeGeneratorMensagemConstantesInstance")
    public ICodeGenerator CodeGeneratorMensagemConstantesInstance() {
        log.info("CodeGeneratorMensagemConstantesInstance :: has started successfully");
        return new CodeGeneratorMensagemConstantes();
    }
    @Bean("CodeGeneratorMensagemResponseInstance")
    public ICodeGenerator CodeGeneratorCodeGeneratorMensagemResponseInstanceInstance() {
        log.info("CodeGeneratorMensagemResponseInstance :: has started successfully");
        return new CodeGeneratorMensagemResponse();
    }
    @Bean("CodeGeneratorCommoditiesBaseExceptionInstance")
    public ICodeGenerator CodeGeneratorCommoditiesBaseExceptionInstance() {
        log.info("CodeGeneratorCommoditiesBaseExceptionInstance :: has started successfully");
        return new CodeGeneratorCommoditiesBaseException();
    }
    @Bean("CodeGeneratorAnalyserExceptionInstance")
    public ICodeGenerator CodeGeneratorAnalyserExceptionInstance() {
        log.info("CodeGeneratorAnalyserExceptionInstance :: has started successfully");
        return new CodeGeneratorAnalyserException();
    }
    @Bean("CodeGeneratorIAnalyserInstance")
    public ICodeGenerator CodeGeneratorIAnalyserInstance() {
        log.info("CodeGeneratorIAnalyserInstance :: has started successfully");
        return new CodeGeneratorIAnalyser();
    }
    @Bean("CodeGeneratorBusinessServiceInstance")
    public ICodeGenerator CodeGeneratorBusinessServiceInstance() {
        log.info("CodeGeneratorBusinessService :: has started successfully");
        return new CodeGeneratorBusinessService();
    }
    @Bean("CodeGeneratorRequestFilterInstance")
    public ICodeGenerator CodeGeneratorRequestFilterInstance() {
        log.info("CodeGeneratorRequestFilterInstance :: has started successfully");
        return new CodeGeneratorRequestFilter();
    }
    @Bean("CodeGeneratorHeadQuarterInstance")
    public ICodeGenerator CodeGeneratorHeadQuarterInstance() {
        log.info("CodeGeneratorHeadQuarterInstance :: has started successfully");
        return new CodeGeneratorHeadQuarter();
    }
    @Bean("CodeGeneratorLogbackInstance")
    public ICodeGenerator CodeGeneratorLogbackInstance() {
        log.info("CodeGeneratorLogbackInstance :: has started successfully");
        return new CodeGeneratorLogback();
    }
}
