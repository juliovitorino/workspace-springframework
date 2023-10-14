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

import com.jwick.continental.deathagreement.model.BetObject;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class BetObjectModelBuilder {

    private BetObject betobject;

    private BetObjectModelBuilder(){}
    public static BetObjectModelBuilder newBetObjectModelTestBuilder() {
        BetObjectModelBuilder builder = new BetObjectModelBuilder();
        builder.betobject = new BetObject();
        return builder;
    }

    public BetObject now() {
        return this.betobject;
    }

    public BetObjectModelBuilder id(Long id){
        this.betobject.setId(id);
        return this;
    }
    public BetObjectModelBuilder who(String who){
        this.betobject.setWho(who);
        return this;
    }
    public BetObjectModelBuilder externalUUID(UUID externalUUID){
        this.betobject.setExternalUUID(externalUUID);
        return this;
    }
    public BetObjectModelBuilder jackpot(Double jackpot){
        this.betobject.setJackpot(jackpot);
        return this;
    }
    public BetObjectModelBuilder jackpotPending(Double jackpotPending){
        this.betobject.setJackpotPending(jackpotPending);
        return this;
    }
    public BetObjectModelBuilder status(String status){
        this.betobject.setStatus(status);
        return this;
    }
    public BetObjectModelBuilder dateCreated(Date dateCreated){
        this.betobject.setDateCreated(dateCreated);
        return this;
    }
    public BetObjectModelBuilder dateUpdated(Date dateUpdated){
        this.betobject.setDateUpdated(dateUpdated);
        return this;
    }


}
