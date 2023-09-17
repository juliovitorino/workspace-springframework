package io.github.cursodsousa.msmensageiro.producer;

import java.util.Map;

public interface IProducerHeader<Input, Output> {
    Output dispatch(Input input, Map<String, Object> mapHeader);
}
