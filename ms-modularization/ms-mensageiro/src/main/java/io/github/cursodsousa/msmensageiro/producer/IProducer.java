package io.github.cursodsousa.msmensageiro.producer;

public interface IProducer<Input, Output> {
    Output dispatch(Input input);
}
