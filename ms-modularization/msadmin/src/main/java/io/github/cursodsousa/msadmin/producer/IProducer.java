package io.github.cursodsousa.msadmin.producer;

public interface IProducer<Input, Output> {
    Output execute(Input input);
}
