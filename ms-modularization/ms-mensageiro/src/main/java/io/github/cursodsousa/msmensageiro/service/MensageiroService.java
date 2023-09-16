package io.github.cursodsousa.msmensageiro.service;

import io.github.cursodsousa.msmensageiro.dto.GeneralRequest;

public interface MensageiroService {
    Boolean sendMessageToAdmin(GeneralRequest generalRequest);
    Boolean sendMessageToMarketing(GeneralRequest generalRequest);
    Boolean sendMessageToFinance(GeneralRequest generalRequest);
}
