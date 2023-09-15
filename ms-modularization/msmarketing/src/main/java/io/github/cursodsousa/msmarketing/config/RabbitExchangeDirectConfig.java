package io.github.cursodsousa.msmarketing.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeDirectConfig {

    @Value("${mq.queues.marketing}")
    private String mqQueueMarketing;

    @Value("${mq.queues.handshake}")
    private String mqQueueMensageiroHandshake;

    @Value("${mq.exchanges.ms.direct.exchangeDefault}")
    private String mqExchangesMsDirectExchangeDefault;

    @Value("${mq.routingKey.mensageiroHandshake}")
    private String mqRoutingKeyMensageiroHandshake;


    // Beans de configuarcao somente das filas
    @Bean(name = "queueMarketing")
    public Queue queueMarketing() {
        return new Queue(mqQueueMarketing,true);
    }

    @Bean(name = "queueMensageiroHandshake")
    public Queue queueMensageiroHandshake() {
        return new Queue(mqQueueMensageiroHandshake,true);
    }

    // Beans de configuracao das exchanges (direct, fanout, headers, topic)
    @Bean(name = "exchangeMsDirect")
    public DirectExchange directExchange() {
        return new DirectExchange(mqExchangesMsDirectExchangeDefault);
    }

    // Beans de configuracao dos binds da fila com sua exchange
    @Bean(name = "mensageiroHandshakeBinding")
    public Binding mensageiroHandshakeBinding(@Qualifier("queueMensageiroHandshake") Queue  mensageiroHandshakeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mensageiroHandshakeQueue).to(exchange).with(mqRoutingKeyMensageiroHandshake);
    }

}
