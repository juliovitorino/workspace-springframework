package br.com.jcv.commons.library.commodities.service;

import java.util.UUID;

public interface BusinessServiceAuth<Input, Output> {
    Output execute(UUID processId, String jwtToken, Input input);
}
