package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DeleteTargetWithNoBetBusinessServiceImpl extends AbstractContinentalServices implements DeleteTargetWithNoBetBusinessService {
    @Override
    public Boolean execute(UUID processId, UUID uuid) {
        log.info("execute :: processId = {} :: is starting", processId);
        List<BetObjectDTO> targets = betObjectService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());
        log.info("execute :: processId = {} :: we've got {} targets to analyse", processId, targets.size());
        long count = 0;
        for (BetObjectDTO target : targets) {
            long diff = DateUtility.getDifferenceDays(dateTime.getToday(), target.getDateCreated());
            if( diff <= config.getPurgePendingBetInDays() && (target.getJackpotPending() + target.getJackpot() == 0)) {
                log.info("execute :: processId = {} :: target ID {} was removed from list after {} day with no bet", processId, target.getId(), diff);
                betObjectService.updateStatusById(target.getId(),"D");
                count++;
            }
        }
        log.info("execute :: processId = {} :: it has been deleted {} targets ", processId, count);
        return true;
    }
}
