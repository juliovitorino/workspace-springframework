package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class ConfirmBetBusinessServiceImpl extends AbstractContinentalServices implements ConfirmBetBusinessService {
    @Override
    @Transactional(transactionManager="transactionManager",
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class,
            noRollbackFor = {BetObjectNotFoundException.class, BetNotFoundException.class}
    )
    public Boolean execute(UUID processId, UUID ticket) {
        log.info("execute :: is starting for ticket => {}", ticket);
        BetDTO betDTO = betService.findBetByTicketAndStatus(ticket, GenericStatusEnums.PENDENTE.getShortValue());
        BetObjectDTO target = betObjectService.findById(betDTO.getIdBetObject());

        betService.updateStatusById(betDTO.getId(), GenericStatusEnums.ATIVO.getShortValue());
        target.setJackpot(target.getJackpot() + betDTO.getBet());
        target.setJackpotPending(target.getJackpotPending() - betDTO.getBet());
        betObjectService.salvar(target);
        log.info("execute :: Your ticket => {} has been activated", ticket);
        return true;
    }
}
