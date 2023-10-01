package br.com.jcv.codegen.codegenerator.config;

import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorBusinessService;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorHeadQuarter;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorIAnalyser;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorLogback;
import br.com.jcv.codegen.codegenerator.factory.codegen.java.CodeGeneratorRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CodeGeneratorFactory {

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
