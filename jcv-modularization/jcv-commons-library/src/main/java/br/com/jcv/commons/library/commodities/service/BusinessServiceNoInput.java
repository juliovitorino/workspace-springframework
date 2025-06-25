package br.com.jcv.commons.library.commodities.service;

import java.util.UUID;

public interface BusinessServiceNoInput<Output> {
    Output execute(UUID processId);
}
