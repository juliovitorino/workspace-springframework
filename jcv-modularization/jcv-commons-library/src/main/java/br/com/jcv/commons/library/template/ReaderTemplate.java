package br.com.jcv.commons.library.template;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ReaderTemplate {

    private final String template;

    public ReaderTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource(templateName);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                template = reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o template: " + templateName, e);
        }
    }

    public static String read(String templateName){
        return (new ReaderTemplate(templateName)).template;
    }
}
