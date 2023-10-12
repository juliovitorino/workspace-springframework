package com.jwick.continental.deathagreement.controller.v1.business.bet;

import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ViewJackpotBusinessServiceImpl extends AbstractContinentalServices implements ViewJackpotBusinessService {
    @Override
    public BetObjectDTO execute(UUID processId, UUID uuidTarget) {
        return betObjectService.findBetObjectByExternalUUIDAndStatus(uuidTarget);
    }
}
