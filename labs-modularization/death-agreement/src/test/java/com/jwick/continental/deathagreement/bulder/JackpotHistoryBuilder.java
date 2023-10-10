package com.jwick.continental.deathagreement.bulder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jwick.continental.deathagreement.constantes.JackpotHistoryConstantes;
import com.jwick.continental.deathagreement.dto.JackpotHistoryDTO;

import java.util.UUID;

public class JackpotHistoryBuilder {

    private JackpotHistoryDTO dto;

    private JackpotHistoryBuilder (){}

    public static JackpotHistoryBuilder newJackpotHistoryTestBuilder() {
        JackpotHistoryBuilder builder = new JackpotHistoryBuilder();
        builder.dto = new JackpotHistoryDTO();
        return builder;
    }
    public JackpotHistoryBuilder id(Long id) {
        this.dto.setId(id);
        return this;
    }

    public JackpotHistoryBuilder description(String description) {
        this.dto.setDescription(description);
        return this;
    }
    public JackpotHistoryBuilder type(String type) {
        this.dto.setType(type);
        return this;
    }
    public JackpotHistoryBuilder betValue(Double betValue) {
        this.dto.setBetValue(betValue);
        return this;
    }
    public JackpotHistoryBuilder ticket(UUID ticket) {
        this.dto.setTicket(ticket);
        return this;
    }
    public JackpotHistoryBuilder idPunter(Long idPunter) {
        this.dto.setIdPunter(idPunter);
        return this;
    }

    public JackpotHistoryDTO now() {
        return this.dto;
    }

}
