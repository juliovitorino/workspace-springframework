package br.com.jcv.commons.library.commodities.service;

import java.util.UUID;
public interface BusinessService<Input, Output> {
    Output execute(UUID processId, Input input);
}
