package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.enums.BetStatusEnum;
import com.jwick.continental.deathagreement.exception.BetCouldntMadeinThePastException;
import com.jwick.continental.deathagreement.exception.BetDeathDateInvalidException;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.exception.BtcAddressNotBelongThisUserException;
import com.jwick.continental.deathagreement.exception.NextBetMustBeDoubleValueOfPreviousBetException;
import com.jwick.continental.deathagreement.exception.NicknameAlreadyInUseException;
import com.jwick.continental.deathagreement.exception.NumberOfBetsAchieveMaximumException;
import com.jwick.continental.deathagreement.exception.PendingBetWaitingTransferFundsException;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CreateBetServiceImpl extends AbstractContinentalServices implements CreateBetService {



    @Override
    public BetResponse execute(UUID processId, BetRequest request) {

        try {
            if(DateUtility.compare(dateTime.getToday(), sdfYMD.parse(request.getDeathDateBet().toString())) == -1) {
                throw new BetCouldntMadeinThePastException("You must to do your bet to the future", HttpStatus.BAD_REQUEST);
            } else {
                Date allowBetDateFrom = DateUtility.addDays(dateTime.getToday(),30);
                Date dateBet = DateUtility.getDate(request.getDeathDateBet());
                if(DateUtility.compare(allowBetDateFrom, dateBet) == -1) {
                    throw new BetDeathDateInvalidException("Your bet date must be after " + allowBetDateFrom, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (ParseException e) {
            throw new CommoditieBaseException("Invalid Parse date => " + request.getDeathDateBet(), HttpStatus.BAD_REQUEST);
        }

        try {
            UserPunterDTO btcAddressCheck = userService.findUserPunterByBtcAddressAndStatus(request.getBtcAddress());
            if(! btcAddressCheck.getNickname().equals(request.getNickname())) {
                throw new BtcAddressNotBelongThisUserException("Other user is using this btc address", HttpStatus.BAD_REQUEST);
            }
        } catch (UserPunterNotFoundException ignored) {
            log.info("ignored UserNotFoundException");
        }

        log.info("execute :: is retrieving last bet for betObject => {}", request.getWhoUUID());
        try {
            UserPunterDTO userPunterDTO = userService.findUserPunterByBtcAddressAndStatus(request.getBtcAddress());
            BetObjectDTO target = betObjectService.findBetObjectByExternalUUIDAndStatus(request.getWhoUUID());
            int yearBet = request.getDeathDateBet().getYear();
            int monthBet = request.getDeathDateBet().getMonthValue();
            List<BetDTO> bets = betService.findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(
                    userPunterDTO.getId(),
                    target.getId(),
                    yearBet,
                    monthBet,
                    GenericStatusEnums.ATIVO.getShortValue()
                    );
            if(bets != null){
                if(!bets.isEmpty()) {
                    if(bets.size() >= config.getMaximumBetsInMonth()) {
                        throw new NumberOfBetsAchieveMaximumException(
                                "Maximum bets in the same month has been achieved. Your total bets is " + bets.size()
                                        + " and maximum allowed is " + config.getMaximumBetsInMonth(),
                                HttpStatus.BAD_REQUEST);
                    }
                    BetDTO lastbet = bets.get(bets.size()-1);
                    if(request.getBet() < lastbet.getBet() * 2) {
                        throw new NextBetMustBeDoubleValueOfPreviousBetException(
                                "Your bet must be double value from the previous bet in the same month.",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                }
            }

        } catch (UserPunterNotFoundException ignored) {
            log.info("execute :: punter will be created.");
        } catch (BetObjectNotFoundException exception) {
            throw new BetObjectNotFoundException("Bet Object does not exist", HttpStatus.BAD_REQUEST);
        }

        log.info("execute :: is checking user information => {}", request.getNickname());
        UserPunterDTO userDTO = checkUserInformationOrCreateIfNew(request);
        if(checkUserStatus(userDTO.getId(), GenericStatusEnums.PENDENTE)) {
            userService.updateStatusById(userDTO.getId(), GenericStatusEnums.ATIVO.getShortValue());
        }

        log.info("execute :: is checking Bet Object for => {}", request.getWhoUUID());
        BetObjectDTO betObjectDTO = isBetObjectExists(request);

        if(Objects.isNull(betObjectDTO)) {
            throw new BetObjectNotFoundException("Bet Object does not exist", HttpStatus.BAD_REQUEST);
        }

        checkDuplicatedPendingBet(userDTO, betObjectDTO);

        log.info("execute :: is creating bet for => {} against {}", request.getNickname(), betObjectDTO.getWho());
        BetDTO betDTO = new BetDTO();
        betDTO.setIdPunter(userDTO.getId());
        betDTO.setIdBetObject(betObjectDTO.getId());
        betDTO.setBet(request.getBet());
        betDTO.setTicket(UUID.randomUUID());
        betDTO.setBitcoinAddress(userDTO.getBtcAddress());
        betDTO.setDeathDate(request.getDeathDateBet());
        BetDTO betSaved = betService.salvar(betDTO);

        log.info("execute :: Adding bet to Jackpot Pending");
        synchronized (betObjectDTO) {
            BetObjectDTO target = betObjectService.findById(betObjectDTO.getId());
            target.setJackpotPending(Objects.isNull(target.getJackpotPending())
                    ?  betSaved.getBet()
                    : target.getJackpotPending() + betSaved.getBet());
            betObjectService.salvar(target);
        }

        BetResponse betResponse = new BetResponse();
        betResponse.setTicket(betSaved.getTicket());
        betResponse.setStatus(BetStatusEnum.fromValue(betSaved.getStatus()).getStatus());
        betResponse.setMessageResponse(MensagemResponse.builder()
                        .mensagem(" Your Bet has been created, but it is Pending beacuse we're waiting "
                                + "you transfer fund to Continental BTC Account => " + config.getContinentalBtcAddress()
                                + ". Your Ticket is " + betSaved.getTicket().toString()
                        )
                .build());
        return betResponse;
    }

    private void checkDuplicatedPendingBet(UserPunterDTO userDTO, BetObjectDTO betObjectDTO) {
        log.info("execute :: is checking for for repeated double bet");
        try {
            betService.findBetByIdPunterAndIdBetObjectAndStatus(userDTO.getId(), betObjectDTO.getId(),GenericStatusEnums.PENDENTE.getShortValue());
            throw new PendingBetWaitingTransferFundsException("Bet is pending and waiting confirmation", HttpStatus.BAD_REQUEST);
        } catch (BetNotFoundException ignored) {
            log.info("execute :: No pending Bet found!");
        }
    }

    private UserPunterDTO checkUserInformationOrCreateIfNew(BetRequest request) {
        UserPunterDTO userDTO = null;
        try {
            userDTO = userService.findUserPunterByNicknameAndStatus(request.getNickname());
            if(! userDTO.getBtcAddress().equals(request.getBtcAddress())) {
                throw new NicknameAlreadyInUseException("Nickname is already in use", HttpStatus.BAD_REQUEST);
            }
        } catch (UserPunterNotFoundException e) {
            userDTO = new UserPunterDTO();
            userDTO.setNickname(request.getNickname());
            userDTO.setBtcAddress(request.getBtcAddress());
            return userService.salvar(userDTO);
        }
        return userDTO;
    }

    private BetObjectDTO isBetObjectExists(BetRequest request) {
        BetObjectDTO betObjectDTO = null;
        try {
            betObjectDTO = betObjectService.findBetObjectByExternalUUIDAndStatus(request.getWhoUUID());
        } catch(BetObjectNotFoundException e) {
            log.info("execute :: New bet object required for => {}", request.getWhoUUID() );
        }
        return betObjectDTO;
    }
}
