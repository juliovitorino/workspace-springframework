package com.jwick.continental.deathagreement.builder;

import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.controller.v1.business.bet.BetRequest;

import java.time.LocalDate;
import java.util.UUID;

public class BetRequestBuilder {

    private BetRequest betRequest;

    private BetRequestBuilder() {}

    public static BetRequestBuilder newBetRequestTestBuilder() {

        BetRequestBuilder builder = new BetRequestBuilder();
        builder.betRequest = new BetRequest();
        builder.betRequest.setBtcAddress("bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh");
        builder.betRequest.setNickname("Mussolini");
        builder.betRequest.setBet(150.0);
        builder.betRequest.setDeathDateBet(DateUtility.getLocalDate(15,7,2100));
        builder.betRequest.setWhoUUID(UUID.fromString("c744d321-d44a-443b-a1ee-fe17af267677"));

        return builder;
    }

    public BetRequestBuilder whoUUID(UUID uuid) {
        betRequest.setWhoUUID(uuid);
        return this;
    }

    public BetRequestBuilder bet(Double bet) {
        betRequest.setBet(bet);
        return this;
    }

    public BetRequestBuilder btcAddress(String btcAddress) {
        betRequest.setBtcAddress(btcAddress);
        return this;
    }

    public BetRequestBuilder nickname(String nickname) {
        betRequest.setNickname(nickname);
        return this;
    }

    public BetRequestBuilder deathDateBet(LocalDate deathDateBet) {
        betRequest.setDeathDateBet(deathDateBet);
        return this;
    }

    public BetRequest now() {
        return betRequest;
    }
}
