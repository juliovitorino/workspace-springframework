package io.github.cursodsousa.msmensageiro.producer.exchange.topic;

import io.github.cursodsousa.msmensageiro.producer.AbstractTopicProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component(value = "financeExchangeTopicProducer")
@Slf4j
public class FinanceExchangeTopicProducer extends AbstractTopicProducer implements IProducer<String,Boolean> {
    public static final String ROUTING_KEY = "input.financeRK";

    public Boolean execute(String dadosPublish) {
        log.info("execute :: will send message to exchange -> {} :: Routing key -> {}", topicExchange.getName(), ROUTING_KEY);
        execute(ROUTING_KEY, dadosPublish + " - " + UUID.randomUUID());
        log.info("execute :: has been sent successfully");
        return true;

    }
}
