/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.jwick.continental.deathagreement.dto;

import br.com.jcv.commons.library.commodities.annotation.RegexValidation;
import br.com.jcv.commons.library.commodities.dto.DTOPadrao;
import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import br.com.jcv.commons.library.utility.RegexValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
* BetDTO - Data Transfer Object
*
* @author Bet
* @since Fri Oct 06 16:12:54 BRT 2023
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BetDTO extends DTOPadrao implements Serializable
{
    @SerializedName(BetConstantes.IDPUNTER)
    @JsonProperty(BetConstantes.IDPUNTER)
    private Long idPunter;

    @SerializedName(BetConstantes.IDBETOBJECT)
    @JsonProperty(BetConstantes.IDBETOBJECT)
    private Long idBetObject;

    @SerializedName(BetConstantes.BET)
    @JsonProperty(BetConstantes.BET)
    private Double bet;

    @SerializedName(BetConstantes.BITCOINADDRESS)
    @JsonProperty(BetConstantes.BITCOINADDRESS)
    @RegexValidation(regex = "[A-Za-z0-9]*")
    private String bitcoinAddress;

    @SerializedName(BetConstantes.TICKET)
    @JsonProperty(BetConstantes.TICKET)
    private UUID ticket;

    @SerializedName(BetConstantes.DEATHDATE)
    @JsonProperty(BetConstantes.DEATHDATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @RegexValidation(regex = "[23][0-9]{3}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])")
    private LocalDate deathDate;


    @SerializedName("mensagemResponse")
    @JsonProperty("mensagemResponse")
    private MensagemResponse mensagemResponse;

    public void setBitcoinAddress(String bitcoinAddress) {
        if(RegexValidator.execute(BetDTO.class,"bitcoinAddress",bitcoinAddress)) {
            this.bitcoinAddress = bitcoinAddress;
        }
    }

    public void setDeathDate(LocalDate deathDate) {
        if(RegexValidator.execute(BetDTO.class,"deathDate",deathDate.toString())) {
            this.deathDate = deathDate;
        }
    }

}
