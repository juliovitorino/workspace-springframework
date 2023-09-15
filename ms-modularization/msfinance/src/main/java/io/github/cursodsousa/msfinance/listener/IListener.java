package io.github.cursodsousa.msfinance.listener;

import org.springframework.messaging.handler.annotation.Payload;

public interface IListener<MessageInput> {
    void onMessage(@Payload MessageInput payload);
}
