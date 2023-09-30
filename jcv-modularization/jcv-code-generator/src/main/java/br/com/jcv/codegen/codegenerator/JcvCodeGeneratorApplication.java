package br.com.jcv.codegen.codegenerator;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JcvCodeGeneratorApplication {

	@Bean
	public CommandLineRunner init(@Autowired @Qualifier("CodeGeneratorBusinessService")ICodeGenerator generator) {
		return args -> {
			generator.generate(Usuario.class);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(JcvCodeGeneratorApplication.class, args);
	}

}
