package io.github.cursodsousa.msavaliadorcredito.exception;

import lombok.Getter;

public class IntegrationErrorClientConsumerException extends RuntimeException {

    @Getter
    private Integer status;

    public IntegrationErrorClientConsumerException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
