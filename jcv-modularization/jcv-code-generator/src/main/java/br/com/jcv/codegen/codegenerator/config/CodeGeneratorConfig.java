package br.com.jcv.codegen.codegenerator.config;

import br.com.jcv.codegen.codegenerator.factory.codegen.CodeGeneratorBusinessService;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CodeGeneratorConfig {

    @Bean("CodeGeneratorBusinessServiceInstance")
    public ICodeGenerator CodeGeneratorBusinessServiceInstance() {
        log.info("CodeGeneratorBusinessService :: has started successfully");
        return new CodeGeneratorBusinessService();
    }
}
