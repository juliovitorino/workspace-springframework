package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.controller.v1.business.betObject.BetObjectResponse;

import java.util.UUID;

public class BetObjectResponseBuilder {

    private BetObjectResponse betObjectResponse;

    private BetObjectResponseBuilder() {}

    public static BetObjectResponseBuilder newBetObjectResponse() {
        BetObjectResponseBuilder builder = new BetObjectResponseBuilder();
        builder.betObjectResponse = new BetObjectResponse();
        builder.betObjectResponse.setWhoUUID(UUID.fromString("c744d321-d44a-443b-a1ee-fe17af267677"));
        return builder;
    }

    public BetObjectResponse now() {
        return this.betObjectResponse;
    }
}
