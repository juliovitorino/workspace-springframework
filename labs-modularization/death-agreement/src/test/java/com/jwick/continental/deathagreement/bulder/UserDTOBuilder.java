package com.jwick.continental.deathagreement.bulder;

import com.jwick.continental.deathagreement.dto.UserDTO;

public class UserDTOBuilder {

    private UserDTO userDTO;

    private UserDTOBuilder() {}

    public static UserDTOBuilder newUserTestBuilder() {
        UserDTOBuilder builder = new UserDTOBuilder();
        builder.userDTO = new UserDTO();
        builder.userDTO.setBtcAddress("bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh");
        builder.userDTO.setNickname("Pacco Rabanne 2020");
        builder.userDTO.setId(1L);
        builder.userDTO.setStatus("P");
        return builder;
    }

    public UserDTOBuilder id(Long id) {
        userDTO.setId(id);
        return this;
    }
    public UserDTOBuilder status(String status) {
        userDTO.setStatus(status);
        return this;
    }

    public UserDTOBuilder btcAddress(String btcAddress) {
        userDTO.setBtcAddress(btcAddress);
        return this;
    }

    public UserDTOBuilder nickname(String nickname) {
        userDTO.setNickname(nickname);
        return this;
    }

    public UserDTO now() {
        return userDTO;
    }

}
