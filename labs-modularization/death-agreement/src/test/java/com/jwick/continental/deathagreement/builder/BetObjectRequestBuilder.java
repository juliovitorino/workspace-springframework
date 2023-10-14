package com.jwick.continental.deathagreement.builder;

import com.jwick.continental.deathagreement.controller.v1.business.betobject.BetObjectRequest;

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
