package io.github.cursodsousa.msmensageiro.config;

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

    @Value("${mq.queues.admin}")
    private String mqQueueAdmin;
    @Value("${mq.queues.finance}")
    private String mqQueueFinance;
    @Value("${mq.queues.marketing}")
    private String mqQueueMarketing;
    @Value("${mq.queues.handshake}")
    private String mqQueueHandshake;
    @Value("${mq.exchanges.ms.direct.exchangeDefault}")
    private String mqExchangesMsDirectExchangeDefault;

    @Value("${mq.routingKey.admin}")
    private String mqRoutingKeyAdmin;

    @Value("${mq.routingKey.finance}")
    private String mqRoutingKeyFinance;

    @Value("${mq.routingKey.marketing}")
    private String mqRoutingKeyMarketing;

    // Beans de configuarcao somente das filas
    @Bean(name = "queueFinance")
    public Queue queueFinance() {
        return new Queue(mqQueueFinance,true);
    }

    @Bean(name = "queueAdmin")
    public Queue queueAdmin() {
        return new Queue(mqQueueAdmin,true);
    }

    @Bean(name = "queueMarketing")
    public Queue queueMarketing() {
        return new Queue(mqQueueMarketing,true);
    }
    @Bean(name = "queueHandshake")
    public Queue queueHandshake() {
        return new Queue(mqQueueHandshake,true);
    }

    // Beans de configuracao das exchanges (direct, fanout, headers, topic)
    @Bean(name = "exchangeMsDirect")
    public DirectExchange directExchange() {
        return new DirectExchange(mqExchangesMsDirectExchangeDefault);
    }

    // Beans de configuracao dos binds da fila com sua exchange
    @Bean(name = "marketingBinding")
    public Binding marketingBinding(@Qualifier("queueMarketing") Queue  marketingQueue, DirectExchange exchange) {
        return BindingBuilder.bind(marketingQueue).to(exchange).with(mqRoutingKeyMarketing);
    }

    @Bean(name = "financeBinding")
    Binding financeBinding(@Qualifier("queueFinance") Queue financeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(financeQueue).to(exchange).with(mqRoutingKeyFinance);
    }

    @Bean(name = "adminBinding")
    Binding adminBinding(@Qualifier("queueFinance") Queue adminQueue, DirectExchange exchange) {
        return BindingBuilder.bind(adminQueue).to(exchange).with(mqRoutingKeyAdmin);
    }
}
