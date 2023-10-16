package com.jwick.continental.deathagreement.service;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateTime;
import com.google.gson.Gson;
import com.jwick.continental.deathagreement.config.ContinentalConfig;
import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;
import com.jwick.continental.deathagreement.model.UserPunter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

public class AbstractContinentalServices {

    protected SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired protected Gson gson;
    @Autowired protected ContinentalConfig config;
    @Autowired protected BetService betService;
    @Autowired protected BetObjectService betObjectService;
    @Autowired protected UserPunterService userService;
    @Autowired protected DateTime dateTime;
    protected boolean checkUserStatus(Long id, GenericStatusEnums status) {
        try {
            UserPunterDTO userDTO = userService.findById(id);
            return userDTO.getStatus().equals(status.getShortValue());
        } catch (UserPunterNotFoundException e) {
            return false;
        }
    }
}
