package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.BetDTO;

import java.util.Date;
import java.util.UUID;

public class BetDTOBuilder {

    private BetDTO betDTO;

    private BetDTOBuilder(){}
    public static BetDTOBuilder newBetDTOTestBuilder() {
        BetDTOBuilder builder = new BetDTOBuilder();
        builder.betDTO = new BetDTO();
        return builder;
    }
    public BetDTOBuilder id(Long id){
        this.betDTO.setId(id);
        return this;
    }
    public BetDTOBuilder idPunter(Long idPunter){
        this.betDTO.setIdPunter(idPunter);
        return this;
    }

    public BetDTOBuilder idBetObject(Long idBetObject) {
        this.betDTO.setIdBetObject(idBetObject);
        return this;
    }

    public BetDTOBuilder bet(Double bet) {
        this.betDTO.setBet(bet);
        return this;
    }

    public BetDTOBuilder bitcoinAddress(String bitcoinAddress) {
        this.betDTO.setBitcoinAddress(bitcoinAddress);
        return this;
    }

    public BetDTOBuilder ticket(UUID ticket) {
        this.betDTO.setTicket(ticket);
        return this;
    }

    public BetDTOBuilder deathDate(Date deathDate) {
        this.betDTO.setDeathDate(deathDate);
        return this;
    }
    public BetDTOBuilder status(String status) {
        this.betDTO.setStatus(status);
        return this;
    }
    public BetDTO now() {
        return this.betDTO;
    }
}
