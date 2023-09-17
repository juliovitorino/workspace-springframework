package io.github.cursodsousa.msmensageiro.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${mq.queues.allInput}")
    private String mqQueueAllInput;
    @Value("${mq.exchanges.ms.direct.exchangeDefault}")
    private String mqExchangesMsDirectExchangeDefault;
    @Value("${mq.exchanges.ms.fanOut.exchangeDefault}")
    private String mqExchangesMsFanOutExchangeDefault;
    @Value("${mq.exchanges.ms.topic.exchangeDefault}")
    private String mqExchangesMsTopicExchangeDefault;
    @Value("${mq.exchanges.ms.header.exchangeDefault}")
    private String mqExchangesMsHeaderExchangeDefault;
    @Value("${mq.routingKey.admin}")
    private String mqRoutingKeyAdmin;
    @Value("${mq.routingKey.finance}")
    private String mqRoutingKeyFinance;
    @Value("${mq.routingKey.marketing}")
    private String mqRoutingKeyMarketing;
    @Value("${mq.routingKey.input.admin}")
    private String mqRoutingKeyInputAdmin;
    @Value("${mq.routingKey.input.finance}")
    private String mqRoutingKeyInputFinance;
    @Value("${mq.routingKey.input.marketing}")
    private String mqRoutingKeyInputMarketing;
    @Value("${mq.routingKey.input.all}")
    private String mqRoutingKeyInputAll;

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

    @Bean(name = "queueAllInput")
    public Queue queueAllInput() {
        return new Queue(mqQueueAllInput,true);
    }

    //
    // REGISTRE OS BEANS DAS SUAS CONGIGURACOES DE EXCHANGES QUE ESTÃO NO RABBIT E ESTE PROJETO PRECISA ACESSAR
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

    @Bean(name = "exchangeMsTopic")
    public TopicExchange topicExchange() {
        return new TopicExchange(mqExchangesMsTopicExchangeDefault);
    }

    @Bean(name = "exchangeMsHeader")
    public HeadersExchange headerExchange() {
        return new HeadersExchange(mqExchangesMsHeaderExchangeDefault);
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


    // Binds das filas para exchange TOPIC
    @Bean(name = "marketingBindingTopic")
    public Binding marketingBindingTopic(@Qualifier("queueMarketing") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyInputMarketing);
    }

    @Bean(name = "financeBindingTopic")
    public Binding financeBindingTopic(@Qualifier("queueFinance") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyInputFinance);
    }

    @Bean(name = "adminBindingTopic")
    public Binding adminBindingTopic(@Qualifier("queueAdmin") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyInputAdmin);
    }

    @Bean(name = "allBindingTopic")
    public Binding allBindingTopic(@Qualifier("queueAllInput") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(mqRoutingKeyInputAll);
    }

    // Bins das filas para exchange HEADER
    @Bean(name = "adminBindingHeader")
    public Binding adminBindingHeader(@Qualifier("queueAdmin") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("department").matches("admin");
    }

    @Bean(name = "financeBindingHeader")
    public Binding financeBindingHeader(@Qualifier("queueFinance") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("department").matches("finance");
    }

    @Bean(name = "marketingBindingHeader")
    public Binding marketingBindingHeader(@Qualifier("queueFinance") Queue queue, HeadersExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).where("department").matches("finance");
    }



}
