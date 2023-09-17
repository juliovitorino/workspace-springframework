package io.github.cursodsousa.msmensageiro.producer.exchange.topic;

import io.github.cursodsousa.msmensageiro.producer.AbstractProducer;
import io.github.cursodsousa.msmensageiro.producer.AbstractTopicProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component(value = "adminExchangeTopicProducer")
@Slf4j
public class AdminExchangeTopicProducer extends AbstractTopicProducer implements IProducer<String,Boolean> {
    public static final String ROUTING_KEY = "input.adminRK";

    @Override
    public Boolean dispatch(String dadosPublish) {
        log.info("dispatch :: will send message to exchange -> {} :: Routing key -> {}", topicExchange.getName(), ROUTING_KEY);
        dispatch(ROUTING_KEY, dadosPublish + " - " + UUID.randomUUID());
        log.info("dispatch :: has been sent successfully");
        return true;

    }
}
