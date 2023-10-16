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

import com.jwick.continental.deathagreement.model.UserPunter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class UserPunterModelBuilder {

    private UserPunter userpunter;

    private UserPunterModelBuilder(){}
    public static UserPunterModelBuilder newUserPunterModelTestBuilder() {
        UserPunterModelBuilder builder = new UserPunterModelBuilder();
        builder.userpunter = new UserPunter();
        return builder;
    }

    public UserPunter now() {
        return this.userpunter;
    }

    public UserPunterModelBuilder id(Long id){
        this.userpunter.setId(id);
        return this;
    }
    public UserPunterModelBuilder nickname(String nickname){
        this.userpunter.setNickname(nickname);
        return this;
    }
    public UserPunterModelBuilder btcAddress(String btcAddress){
        this.userpunter.setBtcAddress(btcAddress);
        return this;
    }
    public UserPunterModelBuilder status(String status){
        this.userpunter.setStatus(status);
        return this;
    }
    public UserPunterModelBuilder dateCreated(Date dateCreated){
        this.userpunter.setDateCreated(dateCreated);
        return this;
    }
    public UserPunterModelBuilder dateUpdated(Date dateUpdated){
        this.userpunter.setDateUpdated(dateUpdated);
        return this;
    }


}
