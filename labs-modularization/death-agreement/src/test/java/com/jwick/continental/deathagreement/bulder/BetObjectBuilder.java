package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;

import java.util.UUID;

public class BetObjectBuilder {

    private static final UUID EXTERNAL_UUID = UUID.fromString("6fa33a6f-6f7a-4edf-90b8-c0d226ade640");
    private BetObjectDTO betObject;

    private BetObjectBuilder() {}

    public static BetObjectBuilder newBetObjectTestBuilder() {
        BetObjectBuilder betObjectBuilder = new BetObjectBuilder();
        betObjectBuilder.betObject = new BetObjectDTO();
        betObjectBuilder.betObject.setId(1L);
        betObjectBuilder.betObject.setExternalUUID(EXTERNAL_UUID);
        betObjectBuilder.betObject.setWho("Target 1");
        return betObjectBuilder;
    }

    public BetObjectBuilder id(Long id) {
        this.betObject.setId(id);
        return this;
    }
    public BetObjectBuilder externalUUID(UUID externalUUID) {
        this.betObject.setExternalUUID(externalUUID);
        return this;
    }
    public BetObjectBuilder who(String who) {
        this.betObject.setWho(who);
        return this;
    }
    public BetObjectDTO now() {
        return betObject;
    }
}
