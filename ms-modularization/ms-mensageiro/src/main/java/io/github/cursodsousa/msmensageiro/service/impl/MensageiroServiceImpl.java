package io.github.cursodsousa.msmensageiro.service.impl;

import com.google.gson.Gson;
import com.netflix.discovery.converters.Auto;
import io.github.cursodsousa.msmensageiro.config.MensageiroConfig;
import io.github.cursodsousa.msmensageiro.dto.GeneralRequest;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducerHeader;
import io.github.cursodsousa.msmensageiro.service.MensageiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementPermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class MensageiroServiceImpl implements MensageiroService {
    @Autowired private @Qualifier("AdminExchangeDirectProducer") IProducer<String,Boolean> adminExchangeDirectProducer;
    @Autowired private @Qualifier("financeExchangeDirectProducer") IProducer<String,Boolean> financeExchangeDirectProducer;
    @Autowired private @Qualifier("marketingExchangeDirectProducer") IProducer<String,Boolean> marketingExchangeDirectProducer;
    @Autowired private @Qualifier("msDefaultExchangeFanoutProducer") IProducer<String,Boolean> defaultExchangeFanoutProducer;
    @Autowired private @Qualifier("adminExchangeTopicProducer") IProducer<String,Boolean> adminExchangeTopicProducer;
    @Autowired private @Qualifier("financeExchangeTopicProducer") IProducer<String,Boolean> financeExchangeTopicProducer;
    @Autowired private @Qualifier("marketingExchangeTopicProducer") IProducer<String,Boolean> marketingExchangeTopicProducer;
    @Autowired private @Qualifier("allExchangeTopicProducer") IProducer<String,Boolean> allExchangeTopicProducer;
    @Autowired private @Qualifier("msDefaultExchangeHeaderProducer") IProducerHeader<String,Boolean> defaultHeaderProducer;
    @Autowired private MensageiroConfig config;
    @Autowired private Gson gson;

    @Override
    public Boolean sendMessageToExchangeTopic(GeneralRequest generalRequest, int type) {
        log.info("sendMessageToExchangeFanOut :: is starting with type {} and request -> {}", type, gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        switch (type) {
            case 0:
                log.info("sendMessageToExchangeTopic :: send to adminExchangeTopicProducer the following message -> {}", message);
                adminExchangeTopicProducer.dispatch(message);
                break;
            case 1:
                log.info("sendMessageToExchangeTopic :: send to financeExchangeTopicProducer the following message -> {}", message);
                financeExchangeTopicProducer.dispatch(message);
                break;
            case 2:
                log.info("sendMessageToExchangeTopic :: send to marketingExchangeTopicProducer the following message -> {}", message);
                marketingExchangeTopicProducer.dispatch(message);
                break;
            default:
                return false;
        }
        log.info("sendMessageToExchangeFanOut :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToExchangeHeaderDepartment(GeneralRequest request, String department) {
        log.info("sendMessageToExchangeHeaderDepartment :: is starting with department = {} and request -> {}", department, gson.toJson(request));
        Map<String,Object> mapHeader = new HashMap<>();
        mapHeader.put("department",department);
        Boolean response = defaultHeaderProducer.dispatch(request.getDataString(), mapHeader);
        log.info("sendMessageToExchangeHeaderDepartment :: message has been sent successfully.");
        return response;
    }

    @Override
    public Boolean sendMessageToExchangeFanOut(GeneralRequest generalRequest) {
        log.info("sendMessageToExchangeFanOut :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToExchangeFanOut :: will send the following message -> {}", message);
        defaultExchangeFanoutProducer.dispatch(message);
        log.info("sendMessageToExchangeFanOut :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToAdmin(GeneralRequest generalRequest) {
        log.info("sendMessageToAdmin :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToAdmin :: will send the following message -> {}", message);
        adminExchangeDirectProducer.dispatch(message);
        log.info("sendMessageToAdmin :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToMarketing(GeneralRequest generalRequest) {
        log.info("sendMessageToMarketing :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToMarketing :: will send the following message -> {}", message);
        marketingExchangeDirectProducer.dispatch(message);
        log.info("sendMessageToMarketing :: message has been sent successfully.");
        return true;
    }

    @Override
    public Boolean sendMessageToFinance(GeneralRequest generalRequest) {
        log.info("sendMessageToFinance :: is starting with request -> {}", gson.toJson(generalRequest));
        final String message = UUID.randomUUID().toString().concat(generalRequest.getDataString());
        log.info("sendMessageToFinance :: will send the following message -> {}", message);
        financeExchangeDirectProducer.dispatch(message);
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
        return rand.nextInt(config.getMensageiroContextProducerExchangeDirectFinanceQtde());
    }
}
