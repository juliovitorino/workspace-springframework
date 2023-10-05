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

import java.text.SimpleDateFormat;
import com.jwick.continental.deathagreement.constantes.GenericConstantes;
import com.jwick.continental.deathagreement.dto.MensagemResponse;
import com.jwick.continental.deathagreement.enums.GenericStatusEnums;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
import com.jwick.continental.deathagreement.dto.RequestFilter;
import com.jwick.continental.deathagreement.repository.BetRepository;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;


/**
* BetServiceImpl - Implementation for Bet interface
*
* @author Bet
* @since Thu Oct 05 10:13:31 BRT 2023
* @copyright(c), Julio Vitorino
*/


@Slf4j
@Service
public class BetServiceImpl implements BetService
{
    @Autowired private BetRepository betRepository;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando Bet com id = {}", id);
        Optional<Bet> betData =
            Optional.ofNullable(betRepository.findById(id)
                .orElseThrow(
                    () -> new BetNotFoundException("Bet não encontrada com id = " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "Bet não encontrada com id = " + String.valueOf(id)))
                    );
        betRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetNotFoundException.class
    )
    public BetDTO salvar(BetDTO betDTO) {
        Date now = new Date();
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
                    () -> new BetNotFoundException("Bet não encontrada " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "Bet com id = " + String.valueOf(id) + " não encontrado."))
                );

        BetDTO response = this.toDTO(betData.get());
        response.setMensagemResponse(new MensagemResponse("MSG-0001","Comando foi executado com sucesso"));

        return response;
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
                    () -> new BetNotFoundException("Bet não encontrada " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "Bet com id = " + String.valueOf(id) + " não encontrado."))
                    );
        if (betData.isPresent()) {
            Bet bet = betData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(BetConstantes.ID)) bet.setId((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.BOUNTY)) bet.setBounty((Double)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.STATUS)) bet.setStatus((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.DATECREATED)) bet.setDateCreated((Date)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetConstantes.DATEUPDATED)) bet.setDateUpdated((Date)entry.getValue());

            if(updates.get(BetConstantes.DATEUPDATED) == null) bet.setDateUpdated(new Date());
            betRepository.save(bet);
            return true;
        }
        return false;
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
                .orElseThrow(() -> new BetNotFoundException("Bet não encontrada com id = " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "Bet não encontrada com id = " + String.valueOf(id)))
                );
        Bet bet = betData.isPresent() ? betData.get() : new Bet();
        bet.setStatus(status);
        bet.setDateUpdated(new Date());
        return toDTO(betRepository.save(bet));

    }

    @Override
    public List<BetDTO> findAllByStatus(String status) {
        List<BetDTO> lstBetDTO = new ArrayList<>();
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
    List<Bet> lstBet = new ArrayList<>();
    Long id = null;
    Double bounty = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(BetConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.BOUNTY)) bounty = (Double) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<Bet> paginaBet = betRepository.findBetByFilter(paging,
        id
        ,bounty
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstBet = paginaBet.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaBet.getNumber());
    response.put("totalItems", paginaBet.getTotalElements());
    response.put("totalPages", paginaBet.getTotalPages());
    response.put("pageBetItems", lstBet.stream().map(m->toDTO(m)).collect(Collectors.toList()));
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
    Double bounty = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(BetConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.BOUNTY)) bounty = (Double) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<Bet> lstBet = betRepository.findBetByFilter(
            id
            ,bounty
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstBet.stream().map(m->toDTO(m)).collect(Collectors.toList());
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
    public List<BetDTO> findAllBetByBountyAndStatus(Double bounty, String status) {
        return betRepository.findAllByBountyAndStatus(bounty, status).stream().map(this::toDTO).collect(Collectors.toList());
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
                    () -> new BetNotFoundException("Bet não encontrada com id = " + id,
                        HttpStatus.NOT_FOUND,
                        "Bet não encontrada com id = " + id))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
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
    public BetDTO findBetByBountyAndStatus(Double bounty, String status) {
        Long maxId = betRepository.loadMaxIdByBountyAndStatus(bounty, status);
        if(maxId == null) maxId = 0L;
        Optional<Bet> betData =
            Optional.ofNullable( betRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetNotFoundException("Bet não encontrada com id = " + bounty,
                        HttpStatus.NOT_FOUND,
                        "Bet não encontrada com bounty = " + bounty))
                );
        return betData.isPresent() ? this.toDTO(betData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetNotFoundException.class
    )
    public BetDTO findBetByBountyAndStatus(Double bounty) {
        return this.findBetByBountyAndStatus(bounty, GenericStatusEnums.ATIVO.getShortValue());
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
                    () -> new BetNotFoundException("Bet não encontrada com id = " + dateCreated,
                        HttpStatus.NOT_FOUND,
                        "Bet não encontrada com dateCreated = " + dateCreated))
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
                    () -> new BetNotFoundException("Bet não encontrada com id = " + dateUpdated,
                        HttpStatus.NOT_FOUND,
                        "Bet não encontrada com dateUpdated = " + dateUpdated))
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
    public BetDTO updateBountyById(Long id, Double bounty) {
        findById(id);
        betRepository.updateBountyById(id, bounty);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        betRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        betRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public BetDTO toDTO(Bet bet) {
        BetDTO betDTO = new BetDTO();
                betDTO.setId(bet.getId());
                betDTO.setBounty(bet.getBounty());
                betDTO.setStatus(bet.getStatus());
                betDTO.setDateCreated(bet.getDateCreated());
                betDTO.setDateUpdated(bet.getDateUpdated());

        return betDTO;
    }

    public Bet toEntity(BetDTO betDTO) {
        Bet bet = null;
        bet = new Bet();
                    bet.setId(betDTO.getId());
                    bet.setBounty(betDTO.getBounty());
                    bet.setStatus(betDTO.getStatus());
                    bet.setDateCreated(betDTO.getDateCreated());
                    bet.setDateUpdated(betDTO.getDateUpdated());

        return bet;
    }
}
