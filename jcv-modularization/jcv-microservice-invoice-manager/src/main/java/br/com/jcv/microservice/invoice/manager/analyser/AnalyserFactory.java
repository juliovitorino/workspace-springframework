package br.com.jcv.microservice.invoice.manager.analyser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AnalyserFactory {

    @Bean("analyserCpf")
    public IAnalyser<String> analyserCpf() {
        log.info("analyserCpf :: Creating bean...");
        IAnalyser<String> instance = new AnalyserCPF();
        log.info("analyserCpf :: bean created at {}", instance.hashCode());
        return instance;
    }
}
