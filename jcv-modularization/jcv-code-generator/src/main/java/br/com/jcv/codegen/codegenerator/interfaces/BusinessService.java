package br.com.jcv.codegen.codegenerator.service;

import java.util.UUID;
public interface BusinessService<Input, Output> {
    Output execute(UUID processId, Input input);
}
