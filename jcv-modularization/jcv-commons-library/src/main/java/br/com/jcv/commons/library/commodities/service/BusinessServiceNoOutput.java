package br.com.jcv.commons.library.commodities.service;

import java.util.UUID;

public interface BusinessServiceNoOutput<Input> {
    void execute(UUID processId, Input input);
}
