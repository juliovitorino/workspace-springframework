package io.github.cursodsousa.msmarketing.producer;

public interface IProducer<Input, Output> {
    Output execute(Input input);
}
