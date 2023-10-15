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

import com.jwick.continental.deathagreement.model.Bet;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class BetModelBuilder {

    private Bet bet;

    private BetModelBuilder(){}
    public static BetModelBuilder newBetModelTestBuilder() {
        BetModelBuilder builder = new BetModelBuilder();
        builder.bet = new Bet();
        builder.bet.setDeathDate(LocalDate.of(2010,5,12));
        return builder;
    }

    public Bet now() {
        return this.bet;
    }

    public BetModelBuilder id(Long id){
        this.bet.setId(id);
        return this;
    }
    public BetModelBuilder idPunter(Long idPunter){
        this.bet.setIdPunter(idPunter);
        return this;
    }
    public BetModelBuilder idBetObject(Long idBetObject){
        this.bet.setIdBetObject(idBetObject);
        return this;
    }
    public BetModelBuilder bet(Double bet){
        this.bet.setBet(bet);
        return this;
    }
    public BetModelBuilder bitcoinAddress(String bitcoinAddress){
        this.bet.setBitcoinAddress(bitcoinAddress);
        return this;
    }
    public BetModelBuilder ticket(UUID ticket){
        this.bet.setTicket(ticket);
        return this;
    }
    public BetModelBuilder deathDate(LocalDate deathDate){
        this.bet.setDeathDate(deathDate);
        return this;
    }
    public BetModelBuilder status(String status){
        this.bet.setStatus(status);
        return this;
    }
    public BetModelBuilder dateCreated(Date dateCreated){
        this.bet.setDateCreated(dateCreated);
        return this;
    }
    public BetModelBuilder dateUpdated(Date dateUpdated){
        this.bet.setDateUpdated(dateUpdated);
        return this;
    }


}
