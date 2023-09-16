package io.github.cursodsousa.msfinance.listener;

import com.google.gson.Gson;
import io.github.cursodsousa.msfinance.producer.IProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class FinanceListener implements IListener<String> {

    @Autowired private @Qualifier("mensageiroHandShakeExchangeDirectProducer") IProducer<String, Boolean> mensageiroProducer;

    @Autowired private Gson gson;

    @Override
    @RabbitListener(queues = "${mq.queues.finance}")
    public void onMessage(@Payload String payload) {
        log.info("onMessage :: message has been received from rabbit -> {}", payload);
        final String handshake = UUID.randomUUID().toString().concat(" - HAND SHAKE OK from financeListener.");
        mensageiroProducer.execute(handshake);
        log.info("onMessage :: your request has been processed.");
    }
}
