/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.jwick.continental.deathagreement.service.impl;

import br.com.jcv.commons.library.commodities.dto.RequestFilter;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import br.com.jcv.commons.library.utility.DateTime;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.repository.BetRepository;
import com.jwick.continental.deathagreement.service.BetService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
* BetServiceImpl - Implementation for Bet interface
*
* @author Bet
* @since Fri Oct 06 16:12:54 BRT 2023
*/


@Slf4j
@Service
public class BetServiceImpl implements BetService
{
    public static final String BET_NOTFOUND_WITH_ID = "Bet não encontrada com id = ";
    public static final String BET_NOTFOUND_WITH_TICKET = "Bet não encontrada com ticket = ";
    public static final String BET_NOTFOUND_WITH_ID_PUNTER = "Bet não encontrada com idPunter = ";
    public static final String BET_NOTFOUND_WITH_ID_BET_OBJECT = "Bet não encontrada com idBetObject = ";
    public static final String BET_NOTFOUND_WITH_BET = "Bet não encontrada com bet = ";
    public static final String BET_NOTFOUND_WITH_BITCOIN_ADDRESS = "Bet não encontrada com bitcoinAddress = ";
    public static final String BET_NOTFOUND_WITH_DEATH_DATE = "Bet não encontrada com deathDate = ";
    public static final String BET_NOTFOUND_WITH_DATECREATED = "Bet não encontrada com dateCreated = ";
    public static final String BET_NOTFOUND_WITH_DATEUPDATED = "Bet não encontrada com dateUpdated = ";
    @Autowired private BetRepository betRepository;
    @Autowired private DateTime dateTime;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando Bet com id = {}", id);
        betRepository.findById(id)
                .orElseThrow(
                        () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID + id,
                                HttpStatus.NOT_FOUND,
                                BET_NOTFOUND_WITH_ID + id));
        betRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public BetDTO salvar(BetDTO betDTO) {
        Date now = dateTime.getToday();
        if(Objects.nonNull(betDTO.getId()) && betDTO.getId() != 0) {
            betDTO.setDateUpdated(now);
        } else {
            betDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            betDTO.setDateCreated(now);
            betDTO.setDateUpdated(now);
        }
        return this.toDTO(betRepository.save(this.toEntity(betDTO)));
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findById(Long id) {
        Optional<Bet> betData =
            Optional.ofNullable(betRepository.findById(id)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID + id,
                    HttpStatus.NOT_FOUND,
                    BET_NOTFOUND_WITH_ID + id))
                );

        return betData.map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<Bet> betData =
            Optional.ofNullable(betRepository.findById(id)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID+id,
                        HttpStatus.NOT_FOUND,
                        BET_NOTFOUND_WITH_ID + id))
                    );
        if (betData.isPresent()) {
            Bet bet = betData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(BetConstantes.IDPUNTER)) bet.setIdPunter((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.IDBETOBJECT)) bet.setIdBetObject((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.BET)) bet.setBet((Double)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.BITCOINADDRESS)) bet.setBitcoinAddress((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.TICKET)) bet.setTicket((UUID)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.DEATHDATE)) bet.setDeathDate((LocalDate) entry.getValue());
            }
            if(updates.get(BetConstantes.DATEUPDATED) == null) bet.setDateUpdated(new Date());
            betRepository.save(bet);
            return true;
        }
        return false;
    }




    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public BetDTO updateStatusById(Long id, String status) {
        Optional<Bet> betData =
            Optional.ofNullable( betRepository.findById(id)
                .orElseThrow(() -> new BetNotFoundException(BET_NOTFOUND_WITH_ID + id,
                    HttpStatus.NOT_FOUND,
                    BET_NOTFOUND_WITH_ID + id))
                );
        Bet bet = betData.orElseGet(Bet::new);
        bet.setStatus(status);
        bet.setDateUpdated(new Date());
        return toDTO(betRepository.save(bet));

    }

    @Override
    public List<BetDTO> findAllByStatus(String status) {
        return betRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<Bet> lstBet;
    Long id = null;
    Long idPunter = null;
    Long idBetObject = null;
    Double bet = null;
    String bitcoinAddress = null;
    UUID ticket = null;
    String deathDate = null;
    String status = null;
    String dateCreated = null;
    String dateUpdated = null;
    SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(BetConstantes.ID)) id = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.IDPUNTER)) idPunter = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.IDBETOBJECT)) idBetObject = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.BET)) bet = Objects.isNull(entry.getValue()) ? null : Double.valueOf(entry.getValue().toString()) ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.BITCOINADDRESS)) bitcoinAddress = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.TICKET)) ticket = Objects.isNull(entry.getValue()) ? null : UUID.fromString(entry.getValue().toString()) ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.STATUS)) status = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DEATHDATE)) deathDate = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString()  ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATECREATED)) dateCreated = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString();
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATEUPDATED)) dateUpdated = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString();

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<Bet> paginaBet = betRepository.findBetByFilter(paging,
        id
        ,idPunter
        ,idBetObject
        ,bet
        ,bitcoinAddress
        ,ticket
        ,deathDate
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstBet = paginaBet.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaBet.getNumber());
    response.put("totalItems", paginaBet.getTotalElements());
    response.put("totalPages", paginaBet.getTotalPages());
    response.put("pageBetItems", lstBet.stream().map(this::toDTO).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
)
    public List<BetDTO> findAllByFilter(RequestFilter filtro) {
        Long id = null;
        Long idPunter = null;
        Long idBetObject = null;
        Double bet = null;
        String bitcoinAddress = null;
        UUID ticket = null;
        String deathDate = null;
        String status = null;
        String dateCreated = null;
        String dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
            if(entry.getKey().equalsIgnoreCase(BetConstantes.ID)) id = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.IDPUNTER)) idPunter = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.IDBETOBJECT)) idBetObject = Objects.isNull(entry.getValue()) ? null : Long.valueOf(entry.getValue().toString()) ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.BET)) bet = Objects.isNull(entry.getValue()) ? null : Double.valueOf(entry.getValue().toString()) ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.BITCOINADDRESS)) bitcoinAddress = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString() ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.TICKET)) ticket = Objects.isNull(entry.getValue()) ? null : UUID.fromString(entry.getValue().toString()) ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.STATUS)) status = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString() ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.DEATHDATE)) deathDate = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString()  ;
            if(entry.getKey().equalsIgnoreCase(BetConstantes.DATECREATED)) dateCreated = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString();
            if(entry.getKey().equalsIgnoreCase(BetConstantes.DATEUPDATED)) dateUpdated = Objects.isNull(entry.getValue()) ? null : entry.getValue().toString();
        }

        List<Bet> lstBet = betRepository.findBetByFilter(
            id
            ,idPunter
            ,idBetObject
            ,bet
            ,bitcoinAddress
            ,ticket
            ,deathDate
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstBet.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByIdAndStatus(Long id, String status) {
        return betRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByIdPunterAndStatus(Long idPunter, String status) {
        return betRepository.findAllByIdPunterAndStatus(idPunter, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByIdBetObjectAndStatus(Long idBetObject, String status) {
        return betRepository.findAllByIdBetObjectAndStatus(idBetObject, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByBetAndStatus(Double bet, String status) {
        return betRepository.findAllByBetAndStatus(bet, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByBitcoinAddressAndStatus(String bitcoinAddress, String status) {
        return betRepository.findAllByBitcoinAddressAndStatus(bitcoinAddress, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByTicketAndStatus(UUID ticket, String status) {
        return betRepository.findAllByTicketAndStatus(ticket, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByDeathDateAndStatus(LocalDate deathDate, String status) {
        return betRepository.findAllByDeathDateAndStatus(deathDate, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(Long idPunter, Long idBetObject, int yearBet, int monthBet, String status) {
        return betRepository.findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(idPunter, idBetObject, yearBet,monthBet, status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByDateCreatedAndStatus(Date dateCreated, String status) {
        return betRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public List<BetDTO> findAllBetByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return betRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdAndStatus(Long id, String status) {
        Long maxId = betRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID + id,
                        HttpStatus.NOT_FOUND,
                        BET_NOTFOUND_WITH_ID + id))
                );
        return betData.map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdAndStatus(Long id) {
        return this.findBetByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdPunterAndStatus(Long idPunter, String status) {
        Long maxId = betRepository.loadMaxIdByIdPunterAndStatus(idPunter, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID_PUNTER + idPunter,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_ID_PUNTER + idPunter))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdPunterAndIdBetObjectAndStatus(Long idPunter, Long idBetObject, String status){
        Long maxId = betRepository.loadMaxIdByIdPunterAndIdBetObjectAndStatus(idPunter, idBetObject, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID_PUNTER + idPunter,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_ID_PUNTER + idPunter))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdPunterAndStatus(Long idPunter) {
        return this.findBetByIdPunterAndStatus(idPunter, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdBetObjectAndStatus(Long idBetObject, String status) {
        Long maxId = betRepository.loadMaxIdByIdBetObjectAndStatus(idBetObject, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_ID_BET_OBJECT + idBetObject,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_ID_BET_OBJECT + idBetObject))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByIdBetObjectAndStatus(Long idBetObject) {
        return this.findBetByIdBetObjectAndStatus(idBetObject, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByBetAndStatus(Double bet, String status) {
        Long maxId = betRepository.loadMaxIdByBetAndStatus(bet, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_BET + bet,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_BET + bet))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByBetAndStatus(Double bet) {
        return this.findBetByBetAndStatus(bet, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByBitcoinAddressAndStatus(String bitcoinAddress, String status) {
        Long maxId = betRepository.loadMaxIdByBitcoinAddressAndStatus(bitcoinAddress, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_BITCOIN_ADDRESS + bitcoinAddress,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_BITCOIN_ADDRESS + bitcoinAddress))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByBitcoinAddressAndStatus(String bitcoinAddress) {
        return this.findBetByBitcoinAddressAndStatus(bitcoinAddress, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByTicketAndStatus(UUID ticket, String status) {
        Long maxId = betRepository.loadMaxIdByTicketAndStatus(ticket, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_TICKET + ticket,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_TICKET + ticket))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByTicketAndStatus(UUID ticket) {
        return this.findBetByTicketAndStatus(ticket, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDeathDateAndStatus(LocalDate deathDate, String status) {
        Long maxId = betRepository.loadMaxIdByDeathDateAndStatus(deathDate, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_DEATH_DATE + deathDate,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_DEATH_DATE + deathDate))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDeathDateAndStatus(LocalDate deathDate) {
        return this.findBetByDeathDateAndStatus(deathDate, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = betRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_DATECREATED + dateCreated,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_DATECREATED + dateCreated))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDateCreatedAndStatus(Date dateCreated) {
        return this.findBetByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = betRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException(BET_NOTFOUND_WITH_DATEUPDATED + dateUpdated,
                        HttpStatus.NOT_FOUND,
                            BET_NOTFOUND_WITH_DATEUPDATED + dateUpdated))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findBetByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateIdPunterById(Long id, Long idPunter) {
        findById(id);
        betRepository.updateIdPunterById(id, idPunter);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateIdBetObjectById(Long id, Long idBetObject) {
        findById(id);
        betRepository.updateIdBetObjectById(id, idBetObject);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateBetById(Long id, Double bet) {
        findById(id);
        betRepository.updateBetById(id, bet);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateBitcoinAddressById(Long id, String bitcoinAddress) {
        findById(id);
        betRepository.updateBitcoinAddressById(id, bitcoinAddress);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateTicketById(Long id, UUID ticket) {
        findById(id);
        betRepository.updateTicketById(id, ticket);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateDeathDateById(Long id, LocalDate deathDate) {
        findById(id);
        betRepository.updateDeathDateById(id, deathDate);
        return findById(id);
    }

    @Override
    public Long countBetsAtDayForBetObject(String deathDateBet, Long idBetObject) {
        return betRepository.countBetsAtDayForBetObject(deathDateBet, idBetObject);
    }

    public BetDTO toDTO(Bet bet) {
        BetDTO betDTO = new BetDTO();
                betDTO.setId(bet.getId());
                betDTO.setIdPunter(bet.getIdPunter());
                betDTO.setIdBetObject(bet.getIdBetObject());
                betDTO.setBet(bet.getBet());
                betDTO.setBitcoinAddress(bet.getBitcoinAddress());
                betDTO.setTicket(bet.getTicket());
                betDTO.setDeathDate(bet.getDeathDate());
                betDTO.setStatus(bet.getStatus());
                betDTO.setDateCreated(bet.getDateCreated());
                betDTO.setDateUpdated(bet.getDateUpdated());

        return betDTO;
    }

    public Bet toEntity(BetDTO betDTO) {
        Bet bet = null;
        bet = new Bet();
                    bet.setId(betDTO.getId());
                    bet.setIdPunter(betDTO.getIdPunter());
                    bet.setIdBetObject(betDTO.getIdBetObject());
                    bet.setBet(betDTO.getBet());
                    bet.setBitcoinAddress(betDTO.getBitcoinAddress());
                    bet.setTicket(betDTO.getTicket());
                    bet.setDeathDate(betDTO.getDeathDate());
                    bet.setStatus(betDTO.getStatus());
                    bet.setDateCreated(betDTO.getDateCreated());
                    bet.setDateUpdated(betDTO.getDateUpdated());

        return bet;
    }
}
