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

import br.com.jcv.commons.library.commodities.constantes.GenericConstantes;
import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.commodities.dto.RequestFilter;

import com.jwick.continental.deathagreement.dto.JackpotHistoryDTO;
import com.jwick.continental.deathagreement.model.JackpotHistory;
import com.jwick.continental.deathagreement.constantes.JackpotHistoryConstantes;
import com.jwick.continental.deathagreement.repository.JackpotHistoryRepository;
import com.jwick.continental.deathagreement.service.JackpotHistoryService;
import com.jwick.continental.deathagreement.exception.JackpotHistoryNotFoundException;

import java.text.SimpleDateFormat;

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
* JackpotHistoryServiceImpl - Implementation for JackpotHistory interface
*
* @author JackpotHistory
* @since Mon Oct 09 08:35:31 BRT 2023
*/


@Slf4j
@Service
public class JackpotHistoryServiceImpl implements JackpotHistoryService
{
    @Autowired private JackpotHistoryRepository jackpothistoryRepository;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando JackpotHistory com id = {}", id);
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable(jackpothistoryRepository.findById(id)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com id = " + String.valueOf(id)))
                    );
        jackpothistoryRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO salvar(JackpotHistoryDTO jackpothistoryDTO) {
        Date now = new Date();
        if(Objects.nonNull(jackpothistoryDTO.getId()) && jackpothistoryDTO.getId() != 0) {
            jackpothistoryDTO.setDateUpdated(now);
        } else {
            jackpothistoryDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            jackpothistoryDTO.setDateCreated(now);
            jackpothistoryDTO.setDateUpdated(now);
        }
        return this.toDTO(jackpothistoryRepository.save(this.toEntity(jackpothistoryDTO)));
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findById(Long id) {
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable(jackpothistoryRepository.findById(id)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "JackpotHistory com id = " + String.valueOf(id) + " não encontrado."))
                );

        JackpotHistoryDTO response = this.toDTO(jackpothistoryData.get());
        response.setMensagemResponse(new MensagemResponse("MSG-0001","Comando foi executado com sucesso"));

        return response;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable(jackpothistoryRepository.findById(id)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory com id = " + String.valueOf(id) + " não encontrado."))
                    );
        if (jackpothistoryData.isPresent()) {
            JackpotHistory jackpothistory = jackpothistoryData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.ID)) jackpothistory.setId((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DESCRIPTION)) jackpothistory.setDescription((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TYPE)) jackpothistory.setType((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.BETVALUE)) jackpothistory.setBetValue((Double)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TICKET)) jackpothistory.setTicket((UUID)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.IDPUNTER)) jackpothistory.setIdPunter((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.STATUS)) jackpothistory.setStatus((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATECREATED)) jackpothistory.setDateCreated((Date)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATEUPDATED)) jackpothistory.setDateUpdated((Date)entry.getValue());

            if(updates.get(JackpotHistoryConstantes.DATEUPDATED) == null) jackpothistory.setDateUpdated(new Date());
            jackpothistoryRepository.save(jackpothistory);
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
        noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO updateStatusById(Long id, String status) {
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository.findById(id)
                .orElseThrow(() -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "JackpotHistory não encontrada com id = " + String.valueOf(id)))
                );
        JackpotHistory jackpothistory = jackpothistoryData.isPresent() ? jackpothistoryData.get() : new JackpotHistory();
        jackpothistory.setStatus(status);
        jackpothistory.setDateUpdated(new Date());
        return toDTO(jackpothistoryRepository.save(jackpothistory));

    }

    @Override
    public List<JackpotHistoryDTO> findAllByStatus(String status) {
        List<JackpotHistoryDTO> lstJackpotHistoryDTO = new ArrayList<>();
        return jackpothistoryRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<JackpotHistory> lstJackpotHistory = new ArrayList<>();
    Long id = null;
    String description = null;
    String type = null;
    Double betValue = null;
    UUID ticket = null;
    Long idPunter = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DESCRIPTION)) description = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TYPE)) type = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.BETVALUE)) betValue = (Double) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TICKET)) ticket = (UUID) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.IDPUNTER)) idPunter = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<JackpotHistory> paginaJackpotHistory = jackpothistoryRepository.findJackpotHistoryByFilter(paging,
        id
        ,description
        ,type
        ,betValue
        ,ticket
        ,idPunter
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstJackpotHistory = paginaJackpotHistory.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaJackpotHistory.getNumber());
    response.put("totalItems", paginaJackpotHistory.getTotalElements());
    response.put("totalPages", paginaJackpotHistory.getTotalPages());
    response.put("pageJackpotHistoryItems", lstJackpotHistory.stream().map(m->toDTO(m)).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
)
    public List<JackpotHistoryDTO> findAllByFilter(RequestFilter filtro) {
    Long id = null;
    String description = null;
    String type = null;
    Double betValue = null;
    UUID ticket = null;
    Long idPunter = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DESCRIPTION)) description = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TYPE)) type = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.BETVALUE)) betValue = (Double) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.TICKET)) ticket = (UUID) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.IDPUNTER)) idPunter = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(JackpotHistoryConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<JackpotHistory> lstJackpotHistory = jackpothistoryRepository.findJackpotHistoryByFilter(
            id
            ,description
            ,type
            ,betValue
            ,ticket
            ,idPunter
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstJackpotHistory.stream().map(m->toDTO(m)).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByIdAndStatus(Long id, String status) {
        return jackpothistoryRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByDescriptionAndStatus(String description, String status) {
        return jackpothistoryRepository.findAllByDescriptionAndStatus(description, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByTypeAndStatus(String type, String status) {
        return jackpothistoryRepository.findAllByTypeAndStatus(type, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByBetValueAndStatus(Double betValue, String status) {
        return jackpothistoryRepository.findAllByBetValueAndStatus(betValue, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByTicketAndStatus(UUID ticket, String status) {
        return jackpothistoryRepository.findAllByTicketAndStatus(ticket, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByIdPunterAndStatus(Long idPunter, String status) {
        return jackpothistoryRepository.findAllByIdPunterAndStatus(idPunter, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByDateCreatedAndStatus(Date dateCreated, String status) {
        return jackpothistoryRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public List<JackpotHistoryDTO> findAllJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return jackpothistoryRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByIdAndStatus(Long id, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + id,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com id = " + id))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByIdAndStatus(Long id) {
        return this.findJackpotHistoryByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDescriptionAndStatus(String description, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByDescriptionAndStatus(description, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + description,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com description = " + description))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDescriptionAndStatus(String description) {
        return this.findJackpotHistoryByDescriptionAndStatus(description, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByTypeAndStatus(String type, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByTypeAndStatus(type, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + type,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com type = " + type))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByTypeAndStatus(String type) {
        return this.findJackpotHistoryByTypeAndStatus(type, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByBetValueAndStatus(Double betValue, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByBetValueAndStatus(betValue, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + betValue,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com betValue = " + betValue))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByBetValueAndStatus(Double betValue) {
        return this.findJackpotHistoryByBetValueAndStatus(betValue, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByTicketAndStatus(UUID ticket, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByTicketAndStatus(ticket, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + ticket,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com ticket = " + ticket))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByTicketAndStatus(UUID ticket) {
        return this.findJackpotHistoryByTicketAndStatus(ticket, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByIdPunterAndStatus(Long idPunter, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByIdPunterAndStatus(idPunter, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + idPunter,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com idPunter = " + idPunter))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByIdPunterAndStatus(Long idPunter) {
        return this.findJackpotHistoryByIdPunterAndStatus(idPunter, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + dateCreated,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com dateCreated = " + dateCreated))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDateCreatedAndStatus(Date dateCreated) {
        return this.findJackpotHistoryByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = jackpothistoryRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<JackpotHistory> jackpothistoryData =
            Optional.ofNullable( jackpothistoryRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new JackpotHistoryNotFoundException("JackpotHistory não encontrada com id = " + dateUpdated,
                        HttpStatus.NOT_FOUND,
                        "JackpotHistory não encontrada com dateUpdated = " + dateUpdated))
                );
        return jackpothistoryData.isPresent() ? this.toDTO(jackpothistoryData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = JackpotHistoryNotFoundException.class
    )
    public JackpotHistoryDTO findJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findJackpotHistoryByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateDescriptionById(Long id, String description) {
        findById(id);
        jackpothistoryRepository.updateDescriptionById(id, description);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateTypeById(Long id, String type) {
        findById(id);
        jackpothistoryRepository.updateTypeById(id, type);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateBetValueById(Long id, Double betValue) {
        findById(id);
        jackpothistoryRepository.updateBetValueById(id, betValue);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateTicketById(Long id, UUID ticket) {
        findById(id);
        jackpothistoryRepository.updateTicketById(id, ticket);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateIdPunterById(Long id, Long idPunter) {
        findById(id);
        jackpothistoryRepository.updateIdPunterById(id, idPunter);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        jackpothistoryRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public JackpotHistoryDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        jackpothistoryRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public JackpotHistoryDTO toDTO(JackpotHistory jackpothistory) {
        JackpotHistoryDTO jackpothistoryDTO = new JackpotHistoryDTO();
                jackpothistoryDTO.setId(jackpothistory.getId());
                jackpothistoryDTO.setDescription(jackpothistory.getDescription());
                jackpothistoryDTO.setType(jackpothistory.getType());
                jackpothistoryDTO.setBetValue(jackpothistory.getBetValue());
                jackpothistoryDTO.setTicket(jackpothistory.getTicket());
                jackpothistoryDTO.setIdPunter(jackpothistory.getIdPunter());
                jackpothistoryDTO.setStatus(jackpothistory.getStatus());
                jackpothistoryDTO.setDateCreated(jackpothistory.getDateCreated());
                jackpothistoryDTO.setDateUpdated(jackpothistory.getDateUpdated());

        return jackpothistoryDTO;
    }

    public JackpotHistory toEntity(JackpotHistoryDTO jackpothistoryDTO) {
        JackpotHistory jackpothistory = null;
        jackpothistory = new JackpotHistory();
                    jackpothistory.setId(jackpothistoryDTO.getId());
                    jackpothistory.setDescription(jackpothistoryDTO.getDescription());
                    jackpothistory.setType(jackpothistoryDTO.getType());
                    jackpothistory.setBetValue(jackpothistoryDTO.getBetValue());
                    jackpothistory.setTicket(jackpothistoryDTO.getTicket());
                    jackpothistory.setIdPunter(jackpothistoryDTO.getIdPunter());
                    jackpothistory.setStatus(jackpothistoryDTO.getStatus());
                    jackpothistory.setDateCreated(jackpothistoryDTO.getDateCreated());
                    jackpothistory.setDateUpdated(jackpothistoryDTO.getDateUpdated());

        return jackpothistory;
    }
}
