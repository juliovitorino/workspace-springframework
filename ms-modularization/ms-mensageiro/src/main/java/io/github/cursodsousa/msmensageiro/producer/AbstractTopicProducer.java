package io.github.cursodsousa.msmensageiro.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.UUID;
@Slf4j
public abstract class AbstractTopicProducer extends AbstractProducer {
    @Autowired protected @Qualifier("exchangeMsTopic") TopicExchange topicExchange;

    protected Boolean execute(String exchange, String routingKey, String dadosPublish) {
        log.info("execute :: will send message to exchange -> {} :: Routing key -> {}", topicExchange.getName(), routingKey);
        rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, dadosPublish);
        log.info("execute :: has been sent successfully");
        return true;

    }
}
