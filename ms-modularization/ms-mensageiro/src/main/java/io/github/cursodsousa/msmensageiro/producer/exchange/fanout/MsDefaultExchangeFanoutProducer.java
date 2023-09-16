package io.github.cursodsousa.msmensageiro.producer.exchange.fanout;

import io.github.cursodsousa.msmensageiro.producer.AbstractProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component(value = "msDefaultExchangeFanoutProducer")
@Slf4j
public class MsDefaultExchangeFanoutProducer extends AbstractProducer implements IProducer<String,Boolean> {
    @Autowired private @Qualifier("exchangeMsFanOut") FanoutExchange exchange;

    public Boolean execute(String dadosPublish) {
        log.info("execute :: wll send data -> {}", dadosPublish);
        log.info("execute :: will send message to exchange -> {}", exchange.getName());
        rabbitTemplate.convertAndSend(exchange.getName(), "",dadosPublish + " - " + UUID.randomUUID());
        log.info("execute :: has been sent successfully");
        return true;

    }
}
