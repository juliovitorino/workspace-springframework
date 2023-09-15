package io.github.cursodsousa.msmensageiro.producer;

import com.google.gson.Gson;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractProducer {
    @Autowired protected RabbitTemplate rabbitTemplate;
    @Autowired protected Gson gson;
}
