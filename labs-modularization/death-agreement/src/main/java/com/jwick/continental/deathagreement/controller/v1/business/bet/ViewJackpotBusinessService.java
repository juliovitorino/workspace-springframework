package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.service.BusinessService;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ViewJackpotBusinessService extends BusinessService<UUID, BetObjectDTO> {
}
