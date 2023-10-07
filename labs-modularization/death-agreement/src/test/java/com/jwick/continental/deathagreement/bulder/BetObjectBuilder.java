package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;

public class BetObjectBuilder {
    private BetObjectDTO betObject;

    private BetObjectBuilder() {}

    public static BetObjectBuilder newBetObject() {
        BetObjectBuilder betObjectBuilder = new BetObjectBuilder();
        betObjectBuilder.betObject = new BetObjectDTO();
        betObjectBuilder.betObject.setWho("Fulano de tal 1");
        return betObjectBuilder;
    }

    public BetObjectDTO now() {
        return betObject;
    }
}
