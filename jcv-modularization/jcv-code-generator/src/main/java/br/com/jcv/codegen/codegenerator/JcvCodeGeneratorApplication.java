package br.com.jcv.codegen.codegenerator;

import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class JcvCodeGeneratorApplication {

	@Bean
	public CommandLineRunner init(
			@Autowired @Qualifier("CodeGeneratorMainStreamInstance") ICodeGeneratorBatch generatorMainStream) {
		return args -> {
			List<WritableCode> codes = generatorMainStream.generate(Usuario.class);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(JcvCodeGeneratorApplication.class, args);
	}

}
