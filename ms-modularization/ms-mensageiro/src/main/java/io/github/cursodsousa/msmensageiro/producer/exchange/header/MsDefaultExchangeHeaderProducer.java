package io.github.cursodsousa.msmensageiro.producer.exchange.header;

import io.github.cursodsousa.msmensageiro.producer.AbstractProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducerHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("msDefaultExchangeHeaderProducer")
public class MsDefaultExchangeHeaderProducer extends AbstractProducer implements IProducerHeader<String,Boolean> {

    @Autowired
    protected @Qualifier("exchangeMsHeader") HeadersExchange headerExchange;

    public Boolean dispatch(String dadosPublish, Map<String,Object> mapHeader) {
        log.info("dispatch :: will send message to exchange -> {} :: mapHeader -> {}", headerExchange.getName(), mapHeader.toString());
        MessageProperties messageProperties = new MessageProperties();

        log.info("dispatch :: is building header message");
        for(Map.Entry<String, Object> entry : mapHeader.entrySet()) {
            log.info("dispatch :: entry = {}, value = {}", entry.getKey(), entry.getValue());
            messageProperties.setHeader(entry.getKey(), entry.getValue());
        }
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage(dadosPublish,messageProperties);
        rabbitTemplate.send(headerExchange.getName(),"", message);
        log.info("dispatch :: has been sent successfully");
        return true;

    }
}
