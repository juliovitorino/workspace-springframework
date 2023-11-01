package br.com.jcv.codegen.codegenerator.config;
import br.com.jcv.codegen.codegenerator.component.CodeGeneratorMainStream;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorIndividual;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAbstractAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserCPF;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorAnalyserException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorApiControllerAdvice;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBuilder;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBuilderModel;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBusinessService;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorCommoditieService;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorCommoditiesBaseException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorController;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorDto;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorDtoPadrao;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericResponse;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorGenericStatusEnum;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorIAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorInvalidFormatException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorLogback;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorMensagemResponse;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorNotFoundException;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRegexConstantes;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRepository;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRequestFilter;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorService;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorServiceImpl;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorServiceImplTest;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorSwaggerConfig;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorTansactionJpaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CodeGeneratorFactory {

    @Bean("CodeGeneratorConstantesTest")
    public ICodeGeneratorIndividual codeGeneratorConstantesTest() {
        log.info("CodeGeneratorConstantesTest :: has started successfully");
        return new CodeGeneratorConstantes();
    }
    @Bean("CodeGeneratorServiceImplTest")
    public ICodeGeneratorIndividual codeGeneratorServiceImplTest() {
        log.info("CodeGeneratorServiceImplTest :: has started successfully");
        return new CodeGeneratorServiceImplTest();
    }
    @Bean("CodeGeneratorTansactionJpaConfigInstance")
    public ICodeGeneratorIndividual codeGeneratorTansactionJpaConfigInstance() {
        log.info("CodeGeneratorTansactionJpaConfigInstance :: has started successfully");
        return new CodeGeneratorTansactionJpaConfig();
    }
    @Bean("CodeGeneratorBuilderInstance")
    public ICodeGeneratorIndividual codeGeneratorBuilderInstance() {
        log.info("CodeGeneratorBuilderInstance :: has started successfully");
        return new CodeGeneratorBuilder();
    }
    @Bean("CodeGeneratorBuilderModelInstance")
    public ICodeGeneratorIndividual codeGeneratorBuilderModelInstance() {
        log.info("CodeGeneratorBuilderModelInstance :: has started successfully");
        return new CodeGeneratorBuilderModel();
    }
    @Bean("CodeGeneratorControllerInstance")
    public ICodeGeneratorIndividual codeGeneratorControllerInstance() {
        log.info("CodeGeneratorControllerInstance :: has started successfully");
        return new CodeGeneratorController();
    }
    @Bean("CodeGeneratorSwaggerConfigInstance")
    public ICodeGeneratorIndividual codeGeneratorSwaggerConfigInstance() {
        log.info("CodeGeneratorSwaggerConfigInstance :: has started successfully");
        return new CodeGeneratorSwaggerConfig();
    }
    @Bean("CodeGeneratorServiceInstance")
    public ICodeGeneratorIndividual codeGeneratorServiceInstance() {
        log.info("CodeGeneratorServiceInstance :: has started successfully");
        return new CodeGeneratorService();
    }
    @Bean("CodeGeneratorServiceImplInstance")
    public ICodeGeneratorIndividual codeGeneratorServiceImplInstance() {
        log.info("CodeGeneratorServiceImplInstance :: has started successfully");
        return new CodeGeneratorServiceImpl();
    }
    @Bean("CodeGeneratorCommoditieServiceInstance")
    public ICodeGeneratorIndividual codeGeneratorCommoditieServiceInstance() {
        log.info("CodeGeneratorCommoditieServiceInstance :: has started successfully");
        return new CodeGeneratorCommoditieService();
    }
    @Bean("CodeGeneratorRepositoryInstance")
    public ICodeGeneratorIndividual codeGeneratorRepositoryInstance() {
        log.info("CodeGeneratorRepositoryInstance :: has started successfully");
        return new CodeGeneratorRepository();
    }
    @Bean("CodeGeneratorDtoPadraoInstance")
    public ICodeGeneratorIndividual codeGeneratorDtoPadraoInstance() {
        log.info("CodeGeneratorDtoPadraoInstance :: has started successfully");
        return new CodeGeneratorDtoPadrao();
    }
    @Bean("CodeGeneratorDtoInstance")
    public ICodeGeneratorIndividual codeGeneratorDtoInstance() {
        log.info("CodeGeneratorDtoInstance :: has started successfully");
        return new CodeGeneratorDto();
    }
    @Bean("CodeGeneratorConstantesInstance")
    public ICodeGeneratorIndividual codeGeneratorConstantesInstance() {
        log.info("CodeGeneratorConstantesInstance :: has started successfully");
        return new CodeGeneratorConstantes();
    }
    @Bean("CodeGeneratorGenericStatusEnumInstance")
    public ICodeGeneratorIndividual codeGeneratorGenericStatusEnumInstance() {
        log.info("CodeGeneratorGenericStatusEnumInstance :: has started successfully");
        return new CodeGeneratorGenericStatusEnum();
    }
    @Bean("CodeGeneratorGenericResponseInstance")
    public ICodeGeneratorIndividual codeGeneratorGenericResponseInstance() {
        log.info("CodeGeneratorGenericResponseInstance :: has started successfully");
        return new CodeGeneratorGenericResponse();
    }
    @Bean("CodeGeneratorApiAdviceInstance")
    public ICodeGeneratorIndividual codeGeneratorApiAdviceInstance() {
        log.info("CodeGeneratorApiAdviceInstance :: has started successfully");
        return new CodeGeneratorApiControllerAdvice();
    }
    @Bean("CodeGeneratorGenericConstantesInstance")
    public ICodeGeneratorIndividual codeGeneratorGenericConstantesInstance() {
        log.info("CodeGeneratorGenericConstantesInstance :: has started successfully");
        return new CodeGeneratorGenericConstantes();
    }
    @Bean("CodeGeneratorRegexConstantesInstance")
    public ICodeGeneratorIndividual codeGeneratorRegexConstantesInstance() {
        log.info("CodeGeneratorRegexConstantesInstance :: has started successfully");
        return new CodeGeneratorRegexConstantes();
    }
    @Bean("CodeGeneratorAnalyserCpfInstance")
    public ICodeGeneratorIndividual codeGeneratorAnalyserCpfInstance() {
        log.info("CodeGeneratorAnalyserCpfInstance :: has started successfully");
        return new CodeGeneratorAnalyserCPF();
    }
    @Bean("CodeGeneratorAbstractAnalyserInstance")
    public ICodeGeneratorIndividual codeGeneratorAbstractAnalyserInstance() {
        log.info("CodeGeneratorAbstractAnalyserInstance :: has started successfully");
        return new CodeGeneratorAbstractAnalyser();
    }
    @Bean("CodeGeneratorMensagemConstantesInstance")
    public ICodeGeneratorIndividual codeGeneratorMensagemConstantesInstance() {
        log.info("CodeGeneratorMensagemConstantesInstance :: has started successfully");
        return new CodeGeneratorMensagemConstantes();
    }
    @Bean("CodeGeneratorMensagemResponseInstance")
    public ICodeGeneratorIndividual codeGeneratorCodeGeneratorMensagemResponseInstanceInstance() {
        log.info("CodeGeneratorMensagemResponseInstance :: has started successfully");
        return new CodeGeneratorMensagemResponse();
    }
    @Bean("CodeGeneratorCommoditiesBaseExceptionInstance")
    public ICodeGeneratorIndividual codeGeneratorCommoditiesBaseExceptionInstance() {
        log.info("CodeGeneratorCommoditiesBaseExceptionInstance :: has started successfully");
        return new CodeGeneratorCommoditiesBaseException();
    }
    @Bean("CodeGeneratorAnalyserExceptionInstance")
    public ICodeGeneratorIndividual codeGeneratorAnalyserExceptionInstance() {
        log.info("CodeGeneratorAnalyserExceptionInstance :: has started successfully");
        return new CodeGeneratorAnalyserException();
    }
    @Bean("CodeGeneratorInvalidFormatExceptionInstance")
    public ICodeGeneratorIndividual codeGeneratorInvalidFormatExceptionInstance() {
        log.info("CodeGeneratorInvalidFormatExceptionInstance :: has started successfully");
        return new CodeGeneratorInvalidFormatException();
    }
    @Bean("CodeGeneratorNotFoundExceptionInstance")
    public ICodeGeneratorIndividual codeGeneratorNotFoundExceptionInstance() {
        log.info("CodeGeneratorNotFoundExceptionInstance :: has started successfully");
        return new CodeGeneratorNotFoundException();
    }
    @Bean("CodeGeneratorIAnalyserInstance")
    public ICodeGeneratorIndividual codeGeneratorIAnalyserInstance() {
        log.info("CodeGeneratorIAnalyserInstance :: has started successfully");
        return new CodeGeneratorIAnalyser();
    }
    @Bean("CodeGeneratorBusinessServiceInstance")
    public ICodeGeneratorIndividual codeGeneratorBusinessServiceInstance() {
        log.info("CodeGeneratorBusinessService :: has started successfully");
        return new CodeGeneratorBusinessService();
    }
    @Bean("CodeGeneratorRequestFilterInstance")
    public ICodeGeneratorIndividual codeGeneratorRequestFilterInstance() {
        log.info("CodeGeneratorRequestFilterInstance :: has started successfully");
        return new CodeGeneratorRequestFilter();
    }
    @Bean("CodeGeneratorMainStreamInstance")
    public ICodeGeneratorBatch codeGeneratorMainStreamInstance() {
        log.info("CodeGeneratorMainStreamInstance :: has started successfully");
        return new CodeGeneratorMainStream();
    }
    @Bean("CodeGeneratorLogbackInstance")
    public ICodeGeneratorIndividual codeGeneratorLogbackInstance() {
        log.info("CodeGeneratorLogbackInstance :: has started successfully");
        return new CodeGeneratorLogback();
    }
}