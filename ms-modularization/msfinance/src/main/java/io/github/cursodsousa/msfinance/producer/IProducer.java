package io.github.cursodsousa.msfinance.producer;

public interface IProducer<Input, Output> {
    Output execute(Input input);
}
