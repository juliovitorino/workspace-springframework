package io.github.cursodsousa.msmensageiro.producer;

public interface IProducer<Input, Output> {
    Output execute(Input input);
}
