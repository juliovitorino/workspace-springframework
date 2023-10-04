package br.com.jcv.microservice.invoice.manager.analyser;

public interface IAnalyser<Input>{
    public void execute(Input input);
}

