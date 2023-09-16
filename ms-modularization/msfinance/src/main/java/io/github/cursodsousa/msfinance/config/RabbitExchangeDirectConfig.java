package io.github.cursodsousa.msfinance.config;

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

    //
    // REGISTRE SUAS PROPRIEDADES DENTRO DO SEU .properties OU .yaml
    //
    @Value("${mq.queues.finance}")
    private String mqQueueFinance;

    @Value("${mq.queues.handshake}")
    private String mqQueueMensageiroHandshake;

    @Value("${mq.exchanges.ms.direct.exchangeDefault}")
    private String mqExchangesMsDirectExchangeDefault;

    @Value("${mq.routingKey.mensageiroHandshake}")
    private String mqRoutingKeyMensageiroHandshake;


    //
    // REGISTRE OS BEANS DAS SUAS FILAS QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    //
    @Bean(name = "queueFinance")
    public Queue queueFinance() {
        return new Queue(mqQueueFinance,true);
    }

    @Bean(name = "queueMensageiroHandshake")
    public Queue queueMensageiroHandshake() {
        return new Queue(mqQueueMensageiroHandshake,true);
    }


    //
    // REGISTRE OS BEANS DAS SUAS EXCHANGES QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    // (direct, fanout, headers, topic)
    //
    @Bean(name = "exchangeMsDirect")
    public DirectExchange directExchange() {
        return new DirectExchange(mqExchangesMsDirectExchangeDefault);
    }

    //
    // REGISTRE OS BEANS QUE RELACIONAM AS EXCHANGES, FILAS E ROUTING KEYS (BIND) QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    //
    @Bean(name = "mensageiroHandshakeBinding")
    public Binding mensageiroHandshakeBinding(@Qualifier("queueMensageiroHandshake") Queue  mensageiroHandshakeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mensageiroHandshakeQueue).to(exchange).with(mqRoutingKeyMensageiroHandshake);
    }

}
