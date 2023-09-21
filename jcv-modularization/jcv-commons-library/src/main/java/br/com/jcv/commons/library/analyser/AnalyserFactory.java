package br.com.jcv.commons.library.analyser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyserFactory {

    @Bean("analyserCpf")
    public IAnalyser<String> analyserCpf() {
        return new AnalyserCPF();
    }
}
