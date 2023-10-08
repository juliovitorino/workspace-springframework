package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.controller.v1.business.bet.BetRequest;
import com.jwick.continental.deathagreement.controller.v1.business.bet.BetResponse;

import java.util.UUID;

public class BetResponseBuilder {

    private BetResponse betResponse;

    private BetResponseBuilder() {}

    public static BetResponseBuilder newBetResponseTestBuilder() {

        BetResponseBuilder builder = new BetResponseBuilder();
        builder.betResponse = new BetResponse();
        builder.betResponse.setTicket(UUID.fromString("b06b9067-f2e7-47f2-a74a-52e86098538f"));
        builder.betResponse.setStatus("P");
        return builder;
    }

    public BetResponseBuilder ticket(UUID ticket) {
        betResponse.setTicket(ticket);
        return this;
    }


    public BetResponse now() {
        return betResponse;
    }
}
