package io.github.cursodsousa.msmensageiro.listener;

import org.springframework.messaging.handler.annotation.Payload;

public interface IListener<MessageInput> {
    void onMessage(@Payload MessageInput payload);
}
