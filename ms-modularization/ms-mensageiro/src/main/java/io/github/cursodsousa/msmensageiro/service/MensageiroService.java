package io.github.cursodsousa.msmensageiro.service;

import io.github.cursodsousa.msmensageiro.dto.GeneralRequest;

public interface MensageiroService {
    Boolean sendMessageToAdmin(GeneralRequest generalRequest);
    Boolean sendMessageToMarketing(GeneralRequest generalRequest);
    Boolean sendMessageToFinance(GeneralRequest generalRequest);
    Boolean sendMessageToAdmin(GeneralRequest generalRequest, boolean isRnd);
    Boolean sendMessageToMarketing(GeneralRequest generalRequest, boolean isRnd);
    Boolean sendMessageToFinance(GeneralRequest generalRequest, boolean isRnd);

    Boolean sendMessageToExchangeFanOut(GeneralRequest generalRequest);
}
