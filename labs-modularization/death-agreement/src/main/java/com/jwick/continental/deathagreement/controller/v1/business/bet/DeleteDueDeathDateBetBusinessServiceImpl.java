package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DeleteDueDeathDateBetBusinessServiceImpl extends AbstractContinentalServices implements DeleteDueDeathDateBetBusinessService {
    @Override
    public Boolean execute(UUID processId, UUID uuid) {
        log.info("execute :: processId = {} :: is starting", processId);

        List<BetDTO> bets = betService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());
        log.info("execute :: processId = {} :: there are {} bets to check", processId, bets.size());
        long count = 0;
        for(BetDTO bet : bets) {
            log.info("execute :: processId = {} :: deathDate = {}", processId, bet.getDeathDate().toString());
            Date deathDateBet = DateUtility.getDate(bet.getDeathDate());
            if(DateUtility.compare(dateTime.getToday(), deathDateBet) == -1) {
                betService.updateStatusById(bet.getId(), "D");
                count++;
            }
        }
        log.info("execute :: processId = {} :: it was processed {} due death date", processId, count);

        return true;
    }
}
