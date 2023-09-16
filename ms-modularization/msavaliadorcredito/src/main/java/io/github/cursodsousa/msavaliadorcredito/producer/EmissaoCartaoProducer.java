package io.github.cursodsousa.msavaliadorcredito.producer;

import com.google.gson.Gson;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosSolicitacaoEmissaoCartao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmissaoCartaoProducer {
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private @Qualifier("queueEmissaoCartoes") Queue queue;
    @Autowired private Gson gson;

    public void execute(DadosSolicitacaoEmissaoCartao dadosPublish) {
        final String json = gson.toJson(dadosPublish);
        log.info("execute :: will send message to queue -> {}", queue.getName());
        log.info("execute :: has started with message -> {}", json);
        rabbitTemplate.convertAndSend(queue.getName(), json);
        log.info("execute :: has been sent successfully");

    }
}
