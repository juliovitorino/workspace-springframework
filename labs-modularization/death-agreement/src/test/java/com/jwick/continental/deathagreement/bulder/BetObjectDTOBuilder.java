package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;

import java.util.UUID;

public class BetObjectDTOBuilder {
    private BetObjectDTO dto;

    private BetObjectDTOBuilder() {}

    public static BetObjectDTOBuilder newBetObjectDTO() {
        BetObjectDTOBuilder builder = new BetObjectDTOBuilder();
        builder.dto = new BetObjectDTO();
        builder.dto.setWho("Jane Doe");
        builder.dto.setExternalUUID(UUID.fromString("c744d321-d44a-443b-a1ee-fe17af267677"));
        builder.dto.setStatus("A");
        return builder;
    }

    public BetObjectDTOBuilder externalUUID(UUID externalUUID) {
        this.dto.setExternalUUID(externalUUID);
        return this;
    }

    public BetObjectDTO now() {
        return this.dto;
    }

}
