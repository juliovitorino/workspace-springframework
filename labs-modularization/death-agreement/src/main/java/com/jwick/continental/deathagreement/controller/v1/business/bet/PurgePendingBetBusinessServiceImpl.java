package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.commodities.service.BusinessService;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PurgePendingBetBusinessServiceImpl extends AbstractContinentalServices implements PurgePendingBetBusinessService {
    @Override
    public Boolean execute(UUID processId, UUID uuid) {
        log.info("execute :: processId = {} :: is checking for pending bet", processId);
        List<BetDTO> pendingBets = betService.findAllByStatus(GenericStatusEnums.PENDENTE.getShortValue());
        log.info("execute :: processId = {} :: we've got {} bets to check", processId,pendingBets.size());
        long count = 0;
        for(BetDTO betDTO : pendingBets) {
            long differenceDays = DateUtility.getDifferenceDays(dateTime.getToday(), betDTO.getDateCreated());
            if(differenceDays <= config.getPurgePendingBetInDays()) {
                log.info("execute :: processId = {} :: deleting bet ID {} - it past {} days that has expired.",processId,betDTO.getId(),differenceDays);
                betService.updateStatusById(betDTO.getId(), "D");
                count++;
            }
        }
        log.info("execute :: processId = {} :: it were deleted {} bets", processId,count);

        return true;
    }

}
