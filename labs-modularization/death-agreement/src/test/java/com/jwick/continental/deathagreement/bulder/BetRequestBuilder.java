package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.controller.v1.business.bet.BetRequest;
import com.jwick.continental.deathagreement.controller.v1.business.betObject.BetObjectRequest;

import java.util.UUID;

public class BetRequestBuilder {

    private BetRequest betRequest;

    private BetRequestBuilder() {}

    public static BetRequestBuilder newBetRequest() {

        BetRequestBuilder builder = new BetRequestBuilder();
        builder.betRequest = new BetRequest();
        builder.betRequest.setBtcAddress("bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh");
        builder.betRequest.setNickname("Mussolini");
        builder.betRequest.setBet(150.0);
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

    public BetRequest now() {
        return betRequest;
    }
}
