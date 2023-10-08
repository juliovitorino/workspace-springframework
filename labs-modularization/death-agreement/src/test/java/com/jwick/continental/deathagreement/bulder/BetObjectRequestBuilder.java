package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.controller.v1.business.betObject.BetObjectRequest;

public class BetObjectRequestBuilder {

    private BetObjectRequest betObjectRequest;

    private BetObjectRequestBuilder() {}

    public static BetObjectRequestBuilder newBetObjectRequestTestBuilder() {

        BetObjectRequestBuilder betObjectRequestBuilder = new BetObjectRequestBuilder();
        betObjectRequestBuilder.betObjectRequest = new BetObjectRequest();
        betObjectRequestBuilder.betObjectRequest.setWho("Jane Doe");
        return betObjectRequestBuilder;
    }

    public BetObjectRequest now() {
        return betObjectRequest;
    }
}
