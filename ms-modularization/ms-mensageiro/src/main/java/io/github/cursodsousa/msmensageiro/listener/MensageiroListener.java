package io.github.cursodsousa.msmensageiro.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MensageiroListener implements IListener<String>{

    @Autowired private Gson gson;

    @Override
    @RabbitListener(queues = "${mq.queues.handshake}")
    public void onMessage(@Payload String payload) {
        log.info("onMessage :: handshake message has been received from rabbit -> {}", payload);
    }
}
