package io.github.cursodsousa.msavaliadorcredito.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${mq.queues.emissao-cartoes}")
    private String mqQueueEmissaoCartoes;

    @Bean(name = "queueEmissaoCartoes")
    public Queue queueEmissaoCartoes() {
        return new Queue(mqQueueEmissaoCartoes,true);
    }

}
