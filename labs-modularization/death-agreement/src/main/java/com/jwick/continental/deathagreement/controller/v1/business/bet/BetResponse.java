package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BetResponse implements Serializable {
    private UUID ticket;
    private String status;
    private MensagemResponse messageResponse;
}
