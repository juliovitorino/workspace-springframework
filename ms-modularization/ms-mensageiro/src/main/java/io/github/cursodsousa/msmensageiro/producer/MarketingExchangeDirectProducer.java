package io.github.cursodsousa.msmensageiro.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component(value = "marketingExchangeDirectProducer")
@Slf4j
public class MarketingExchangeDirectProducer extends AbstractProducer implements IProducer<String,Boolean> {
    public static final String ROUTING_KEY = "marketingRK";
    @Autowired private @Qualifier("exchangeMsDirect") DirectExchange directExchange;

    public Boolean execute(String dadosPublish) {
        log.info("execute :: will send message to exchange -> {} :: Routing key -> {}", directExchange.getName(), ROUTING_KEY);
        rabbitTemplate.convertAndSend(directExchange.getName(), ROUTING_KEY, dadosPublish + " - " + UUID.randomUUID());
        log.info("execute :: has been sent successfully");
        return true;

    }
}
