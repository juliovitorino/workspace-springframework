package com.jwick.continental.deathagreement.builder;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;

import java.util.UUID;

public class BetObjectDTOBuilder {
    private BetObjectDTO dto;

    private BetObjectDTOBuilder() {}

    public static BetObjectDTOBuilder newBetObjectDTOTestBuilder() {
        BetObjectDTOBuilder builder = new BetObjectDTOBuilder();
        builder.dto = new BetObjectDTO();
        builder.dto.setWho("Jane Doe");
        builder.dto.setJackpotPending(0.0);
        builder.dto.setJackpot(0.0);
        return builder;
    }

    public BetObjectDTOBuilder status(String status) {
        this.dto.setStatus(status);
        return this;
    }

    public BetObjectDTOBuilder jackpotPending(Double jackpotPending) {
        this.dto.setJackpotPending(jackpotPending);
        return this;
    }
    public BetObjectDTOBuilder jackpot(Double jackpot) {
        this.dto.setJackpot(jackpot);
        return this;
    }

    public BetObjectDTOBuilder who(String who) {
        this.dto.setWho(who);
        return this;
    }

    public BetObjectDTOBuilder externalUUID(UUID externalUUID) {
        this.dto.setExternalUUID(externalUUID);
        return this;
    }

    public BetObjectDTO now() {
        return this.dto;
    }

}
