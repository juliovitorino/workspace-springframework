package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ConfirmBetBusinessService extends BusinessService<UUID, Boolean> {
}
