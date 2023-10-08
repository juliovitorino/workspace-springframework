package com.jwick.continental.deathagreement.controller.v1.business.betObject;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BetObjectBusinessServiceImpl extends AbstractContinentalServices implements BetObjectBusinessService {
    @Override
    public BetObjectResponse execute(UUID processId, BetObjectRequest request) {
        log.info("execute :: running processId =. {}",processId);
        BetObjectDTO betObjectDTO = new BetObjectDTO();
        betObjectDTO.setWho(request.getWho());
        betObjectDTO.setExternalUUID(UUID.randomUUID());

        BetObjectDTO saved = betObjectService.salvar(betObjectDTO);
        betObjectService.updateStatusById(saved.getId(), GenericStatusEnums.ATIVO.getShortValue());

        return BetObjectResponse.builder()
                .whoUUID(saved.getExternalUUID())
                .build();
    }
}
