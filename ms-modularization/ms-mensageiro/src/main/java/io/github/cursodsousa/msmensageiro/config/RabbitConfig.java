package io.github.cursodsousa.msmensageiro.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.util.ConditionalOnBootstrapEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    //
    // VARIAVES CRIADAS DENTRO DO SEU ARQUIVO .properties OU .yml
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
    @Value("${mq.exchanges.ms.fanOut.exchangeDefault}")
    private String mqExchangesMsFanOutExchangeDefault;
    @Value("${mq.routingKey.admin}")
    private String mqRoutingKeyAdmin;

    @Value("${mq.routingKey.finance}")
    private String mqRoutingKeyFinance;

    @Value("${mq.routingKey.marketing}")
    private String mqRoutingKeyMarketing;

    // ATENCAO: Os registros dos beans abaixo NAO CRIAM AS FILAS, EXCHANGES E BINDS DE ROUTING KEYS no RabbitMQ

    //
    // REGISTRE OS BEANS DAS SUAS FILAS QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    //

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

    //
    // REGISTRE OS BEANS DAS SUAS EXCHANGES QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    // (direct, fanout, headers, topic)
    //

    @Bean(name = "exchangeMsDirect")
    public DirectExchange directExchange() {
        return new DirectExchange(mqExchangesMsDirectExchangeDefault);
    }

    @Bean(name = "exchangeMsFanOut")
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(mqExchangesMsFanOutExchangeDefault);
    }

    //
    // REGISTRE OS BEANS QUE RELACIONAM AS EXCHANGES, FILAS E ROUTING KEYS (BIND) QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
    //

    // binds das filas  para exchange direct
    @Bean(name = "marketingBinding")
    public Binding marketingBindingDirect(@Qualifier("queueMarketing") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyMarketing);
    }

    @Bean(name = "financeBinding")
    public Binding financeBindingDirect(@Qualifier("queueFinance") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyFinance);
    }

    @Bean(name = "adminBinding")
    public Binding adminBindingDirect(@Qualifier("queueAdmin") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyAdmin);
    }

    // Binds das filas para exchange fanOut
    @Bean(name = "marketingBindingFanOut")
    public Binding marketingBindingFanOut(@Qualifier("queueMarketing") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean(name = "financeBindingFanOut")
    public Binding financeBindingFanOut(@Qualifier("queueFinance") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean(name = "adminBindingFanOut")
    public Binding adminBindingFanOut(@Qualifier("queueAdmin") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

}
