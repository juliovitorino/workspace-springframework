package io.github.cursodsousa.msavaliadorcredito.producer;

import com.google.gson.Gson;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosSolicitacaoEmissaoCartao;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class EmissaoCartaoProducer {
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private @Qualifier("queueEmissaoCartoes") Queue queue;
    @Autowired private Gson gson;

    public void execute(DadosSolicitacaoEmissaoCartao dadosPublish) {
        rabbitTemplate.convertAndSend(queue.getName(), gson.toJson(dadosPublish));
    }
}
