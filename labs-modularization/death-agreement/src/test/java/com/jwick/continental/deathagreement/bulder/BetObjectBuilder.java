package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;

import java.util.UUID;

public class BetObjectBuilder {
    private BetObjectDTO betObject;

    private BetObjectBuilder() {}

    public static BetObjectBuilder newBetObjectTestBuilder() {
        BetObjectBuilder betObjectBuilder = new BetObjectBuilder();
        betObjectBuilder.betObject = new BetObjectDTO();
        betObjectBuilder.betObject.setId(1L);
        betObjectBuilder.betObject.setExternalUUID(UUID.fromString("6fa33a6f-6f7a-4edf-90b8-c0d226ade640"));
        betObjectBuilder.betObject.setWho("Fulano de tal 1");
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
