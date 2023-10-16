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


package com.jwick.continental.deathagreement.builder;

import com.jwick.continental.deathagreement.dto.BetDTO;

import java.time.LocalDate;
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

    public BetDTO now() {
        return this.betDTO;
    }

    public BetDTOBuilder id(Long id){
        this.betDTO.setId(id);
        return this;
    }
    public BetDTOBuilder idPunter(Long idPunter){
        this.betDTO.setIdPunter(idPunter);
        return this;
    }
    public BetDTOBuilder idBetObject(Long idBetObject){
        this.betDTO.setIdBetObject(idBetObject);
        return this;
    }
    public BetDTOBuilder bet(Double bet){
        this.betDTO.setBet(bet);
        return this;
    }
    public BetDTOBuilder bitcoinAddress(String bitcoinAddress){
        this.betDTO.setBitcoinAddress(bitcoinAddress);
        return this;
    }
    public BetDTOBuilder ticket(UUID ticket){
        this.betDTO.setTicket(ticket);
        return this;
    }
    public BetDTOBuilder deathDate(LocalDate deathDate){
        this.betDTO.setDeathDate(deathDate);
        return this;
    }
    public BetDTOBuilder status(String status){
        this.betDTO.setStatus(status);
        return this;
    }
    public BetDTOBuilder dateCreated(Date dateCreated){
        this.betDTO.setDateCreated(dateCreated);
        return this;
    }
    public BetDTOBuilder dateUpdated(Date dateUpdated){
        this.betDTO.setDateUpdated(dateUpdated);
        return this;
    }


}
