package com.jwick.continental.deathagreement.controller.v1.bet;

import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.enums.BetStatusEnum;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.exception.BtcAddressNotBelongThisUserException;
import com.jwick.continental.deathagreement.exception.NicknameAlreadyInUseException;
import com.jwick.continental.deathagreement.exception.PendingBetWaitingTransferFundsException;
import com.jwick.continental.deathagreement.exception.PendingUserStatusException;
import com.jwick.continental.deathagreement.exception.UserNotFoundException;
import com.jwick.continental.deathagreement.service.AbstractContinentalServices;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CreateBetServiceImpl extends AbstractContinentalServices implements CreateBetService {


    @Override
    public BetResponse execute(UUID processId, BetRequest request) {
        try {
            UserDTO btcAddressCheck = userService.findUserByBtcAddressAndStatus(request.getBtcAddress());
            if(! btcAddressCheck.getNickname().equals(request.getNickname())) {
                throw new BtcAddressNotBelongThisUserException("Other user is using this btc address", HttpStatus.BAD_REQUEST);
            }
        } catch (UserNotFoundException ignored) {}

        log.info("execute :: is checking user information => {}", request.getNickname());
        UserDTO userDTO = CheckUserInformationOrCreateIfNew(request);
        if(checkUserStatus(userDTO.getId(), GenericStatusEnums.PENDENTE)) {
            userService.updateStatusById(userDTO.getId(), GenericStatusEnums.ATIVO.getShortValue());
        }

        log.info("execute :: is checking Bet Object for => {}", request.getWho());
        BetObjectDTO betObjectDTO = isBetObjectExists(request);

        if(Objects.isNull(betObjectDTO)) {
            betObjectDTO = new BetObjectDTO();
            betObjectDTO.setWho(request.getWho());
            betObjectDTO.setExternalUUID(UUID.randomUUID());
            betObjectDTO = betObjectService.salvar(betObjectDTO);
            betObjectService.updateStatusById(betObjectDTO.getId(),GenericStatusEnums.ATIVO.getShortValue());
        }

        checkDuplicatedPendingBet(userDTO, betObjectDTO);

        log.info("execute :: is creating bet for => {} against {}", request.getNickname(), request.getWho());
        BetDTO betDTO = new BetDTO();
        betDTO.setIdPunter(userDTO.getId());
        betDTO.setIdBetObject(betObjectDTO.getId());
        betDTO.setBet(request.getBet());
        betDTO.setTicket(UUID.randomUUID());
        betDTO.setBitcoinAddress(userDTO.getBtcAddress());
        betDTO.setDeathDate(request.getDeathDateBet());
        BetDTO betSaved = betService.salvar(betDTO);

        BetResponse betResponse = new BetResponse();
        betResponse.setTicket(betSaved.getTicket());
        betResponse.setStatus(BetStatusEnum.fromValue(betSaved.getStatus()).getStatus());
        betResponse.setMessageResponse(MensagemResponse.builder()
                        .mensagem(" Your Bet has been created, but it is Pending beacuse we're waiting you transfer fund to Continental BTC Account => " + config.getContinentalBtcAddress()
                        + ". Your Ticket is " + betSaved.getTicket()
                        )
                .build());
        return betResponse;
    }

    private void checkDuplicatedPendingBet(UserDTO userDTO, BetObjectDTO betObjectDTO) {
        log.info("execute :: is checking for for repeated double bet");
        try {
            BetDTO doubleBet = betService.findBetByIdPunterAndIdBetObjectAndStatus(userDTO.getId(), betObjectDTO.getId(),GenericStatusEnums.PENDENTE.getShortValue());
            throw new PendingBetWaitingTransferFundsException("Bet is pending and waiting confirmation", HttpStatus.BAD_REQUEST);
        } catch (BetNotFoundException e) {
            log.info("execute :: No pending Bet found!");
        }
    }

    private UserDTO CheckUserInformationOrCreateIfNew(BetRequest request) {
        UserDTO userDTO = null;
        try {
            userDTO = userService.findUserByNicknameAndStatus(request.getNickname());
            if(! userDTO.getBtcAddress().equals(request.getBtcAddress())) {
                throw new NicknameAlreadyInUseException("Nickname is already in use", HttpStatus.BAD_REQUEST);
            }
        } catch (UserNotFoundException e) {
            userDTO = new UserDTO();
            userDTO.setNickname(request.getNickname());
            userDTO.setBtcAddress(request.getBtcAddress());
            return userService.salvar(userDTO);
        }
        return userDTO;
    }

    private BetObjectDTO isBetObjectExists(BetRequest request) {
        BetObjectDTO betObjectDTO = null;
        try {
            betObjectDTO = betObjectService.findBetObjectByWhoAndStatus(request.getWho());
        } catch(BetObjectNotFoundException e) {
            log.info("execute :: New bet object required for => {}", request.getWho() );
        }
        return betObjectDTO;
    }
}
