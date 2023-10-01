package br.com.jcv.codegen.codegenerator;

import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGenerator;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
public class JcvCodeGeneratorApplication {

	@Bean
	public CommandLineRunner init(
			@Autowired @Qualifier("CodeGeneratorHeadQuarterInstance") ICodeGenerator generatorHeadQuarter) {
		return args -> {
			generatorHeadQuarter.generate(Usuario.class);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(JcvCodeGeneratorApplication.class, args);
	}

}
