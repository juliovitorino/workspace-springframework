package com.jwick.continental.deathagreement.service;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import com.google.gson.Gson;
import com.jwick.continental.deathagreement.config.ContinentalConfig;
import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractContinentalServices {

    @Autowired protected Gson gson;
    @Autowired protected ContinentalConfig config;
    @Autowired protected BetService betService;
    @Autowired protected BetObjectService betObjectService;
    @Autowired protected UserService userService;
    protected boolean checkUserStatus(Long id, GenericStatusEnums status) {
        try {
            UserDTO userDTO = userService.findById(id);
            return userDTO.getStatus().equals(status.getShortValue());
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
