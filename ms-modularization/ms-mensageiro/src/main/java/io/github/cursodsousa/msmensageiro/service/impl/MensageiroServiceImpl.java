package io.github.cursodsousa.msmensageiro.service.impl;

import com.google.gson.Gson;
import io.github.cursodsousa.msmensageiro.dto.GeneralRequest;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import io.github.cursodsousa.msmensageiro.service.MensageiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class MensageiroServiceImpl implements MensageiroService {
    @Autowired private @Qualifier("AdminExchangeDirectProducer") IProducer<String,Boolean> adminExchangeDirectProducer;
    @Autowired private @Qualifier("financeExchangeDirectProducer") IProducer<String,Boolean> financeExchangeDirectProducer;
    @Autowired private @Qualifier("marketingExchangeDirectProducer") IProducer<String,Boolean> marketingExchangeDirectProducer;
    @Autowired private @Qualifier("msDefaultExchangeFanoutProducer") IProducer<String,Boolean> defaultExchangeFanoutProducer;
    @Autowired private Gson gson;

    @Override
    public Boolean sendMessageToExchangeFanOut(GeneralRequest generalRequest) {
        log.info("sendMessageToExchangeFanOut :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToExchangeFanOut :: will send the following message -> {}", message);
        defaultExchangeFanoutProducer.execute(message);
        log.info("sendMessageToExchangeFanOut :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToAdmin(GeneralRequest generalRequest) {
        log.info("sendMessageToAdmin :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToAdmin :: will send the following message -> {}", message);
        adminExchangeDirectProducer.execute(message);
        log.info("sendMessageToAdmin :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToMarketing(GeneralRequest generalRequest) {
        log.info("sendMessageToMarketing :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToMarketing :: will send the following message -> {}", message);
        marketingExchangeDirectProducer.execute(message);
        log.info("sendMessageToMarketing :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToFinance(GeneralRequest generalRequest) {
        log.info("sendMessageToFinance :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToFinance :: will send the following message -> {}", message);
        financeExchangeDirectProducer.execute(message);
        log.info("sendMessageToFinance :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToAdmin(GeneralRequest generalRequest, boolean isRnd) {
        int totalMessages = isRnd ? getRnd() : 1;
        for (int i = 0; i < totalMessages; i++) {
            this.sendMessageToAdmin(generalRequest);
        }
        return true;
    }

    @Override
    public Boolean sendMessageToMarketing(GeneralRequest generalRequest, boolean isRnd) {
        int totalMessages = isRnd ? getRnd() : 1;
        for (int i = 0; i < totalMessages; i++) {
            this.sendMessageToMarketing(generalRequest);
        }
        return true;
    }

    @Override
    public Boolean sendMessageToFinance(GeneralRequest generalRequest, boolean isRnd) {
        int totalMessages = isRnd ? getRnd() : 1;
        for (int i = 0; i < totalMessages; i++) {
            this.sendMessageToFinance(generalRequest);
        }
        return true;
    }

    private int getRnd() {
        Random rand = new Random();
        return rand.nextInt(500);
    }
}
