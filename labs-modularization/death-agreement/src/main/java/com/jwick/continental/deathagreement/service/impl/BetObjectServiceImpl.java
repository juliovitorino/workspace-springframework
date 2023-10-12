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
import com.jwick.continental.deathagreement.constantes.BetObjectConstantes;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.model.BetObject;
import com.jwick.continental.deathagreement.repository.BetObjectRepository;
import com.jwick.continental.deathagreement.service.BetObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
* BetObjectServiceImpl - Implementation for BetObject interface
*
* @author BetObject
* @since Fri Oct 06 12:17:44 BRT 2023
*/


@Slf4j
@Service
public class BetObjectServiceImpl implements BetObjectService
{
    public static final String BET_OBJECT_NOTFOUND_WITH_ID = "BetObject não encontrada com id = ";
    public static final String BET_OBJECT_NOTFOUND_WITH_WHO = "BetObject não encontrada com who = ";
    public static final String BET_OBJECT_NOTFOUND_WITH_DATECREATED = "BetObject não encontrada com dateCreated = ";
    public static final String BET_OBJECT_NOTFOUND_WITH_DATEUPDATED = "BetObject não encontrada com dateUpdated = ";
    public static final String BET_OBJECT_NOTFOUND_WITH_EXTERNALUUID = "BetObject não encontrada com externalUUID = ";
    @Autowired private BetObjectRepository betobjectRepository;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetObjectNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando BetObject com id = {}", id);
        Optional<BetObject> betobjectData =
            Optional.ofNullable(betobjectRepository.findById(id)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_ID + id,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_ID + id))
                    );
        betobjectRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO salvar(BetObjectDTO betobjectDTO) {
        Date now = new Date();
        if(Objects.nonNull(betobjectDTO.getId()) && betobjectDTO.getId() != 0) {
            betobjectDTO.setDateUpdated(now);
        } else {
            betobjectDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            betobjectDTO.setDateCreated(now);
            betobjectDTO.setDateUpdated(now);
        }
        return this.toDTO(betobjectRepository.save(this.toEntity(betobjectDTO)));
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findById(Long id) {
        Optional<BetObject> betobjectData =
            Optional.ofNullable(betobjectRepository.findById(id)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_ID + id,
                    HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_ID+ id ))
                );
        return betobjectData.map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetObjectNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<BetObject> betobjectData =
            Optional.ofNullable(betobjectRepository.findById(id)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_ID + id,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_ID + id))
                    );
        if (betobjectData.isPresent()) {
            BetObject betobject = betobjectData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.WHO)) betobject.setWho((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.EXTERNALUUID)) betobject.setExternalUUID((UUID)entry.getValue());
            }
            if(updates.get(BetObjectConstantes.DATEUPDATED) == null) betobject.setDateUpdated(new Date());
            betobjectRepository.save(betobject);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO updateStatusById(Long id, String status) {
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository.findById(id)
                .orElseThrow(() -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_ID +id,
                    HttpStatus.NOT_FOUND,
                        BET_OBJECT_NOTFOUND_WITH_ID + id))
                );
        BetObject betobject = betobjectData.isPresent() ? betobjectData.get() : new BetObject();
        betobject.setStatus(status);
        betobject.setDateUpdated(new Date());
        return toDTO(betobjectRepository.save(betobject));

    }

    @Override
    public List<BetObjectDTO> findAllByStatus(String status) {
        return betobjectRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<BetObject> lstBetObject = new ArrayList<>();
    Long id = null;
    String who = null;
    UUID externalUUID = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.WHO)) who = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.EXTERNALUUID)) externalUUID = (UUID) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<BetObject> paginaBetObject = betobjectRepository.findBetObjectByFilter(paging,
        id
        ,who
        ,externalUUID
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstBetObject = paginaBetObject.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaBetObject.getNumber());
    response.put("totalItems", paginaBetObject.getTotalElements());
    response.put("totalPages", paginaBetObject.getTotalPages());
    response.put("pageBetObjectItems", lstBetObject.stream().map(m->toDTO(m)).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
)
    public List<BetObjectDTO> findAllByFilter(RequestFilter filtro) {
    Long id = null;
    String who = null;
    UUID externalUUID = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.WHO)) who = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.EXTERNALUUID)) externalUUID = (UUID) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(BetObjectConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<BetObject> lstBetObject = betobjectRepository.findBetObjectByFilter(
            id
            ,who
            ,externalUUID
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstBetObject.stream().map(m->toDTO(m)).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public List<BetObjectDTO> findAllBetObjectByIdAndStatus(Long id, String status) {
        return betobjectRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public List<BetObjectDTO> findAllBetObjectByWhoAndStatus(String who, String status) {
        return betobjectRepository.findAllByWhoAndStatus(who, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public List<BetObjectDTO> findAllBetObjectByExternalUUIDAndStatus(UUID externalUUID, String status) {
        return betobjectRepository.findAllByExternalUUIDAndStatus(externalUUID, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public List<BetObjectDTO> findAllBetObjectByDateCreatedAndStatus(Date dateCreated, String status) {
        return betobjectRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public List<BetObjectDTO> findAllBetObjectByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return betobjectRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByIdAndStatus(Long id, String status) {
        Long maxId = betobjectRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_ID + id,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_ID + id))
                );
        return betobjectData.isPresent() ? this.toDTO(betobjectData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByIdAndStatus(Long id) {
        return this.findBetObjectByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByWhoAndStatus(String who, String status) {
        Long maxId = betobjectRepository.loadMaxIdByWhoAndStatus(who, status);
        if(maxId == null) maxId = 0L;
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_WHO + who,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_WHO + who))
                );
        return betobjectData.isPresent() ? this.toDTO(betobjectData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByWhoAndStatus(String who) {
        return this.findBetObjectByWhoAndStatus(who, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByExternalUUIDAndStatus(UUID externalUUID, String status) {
        Long maxId = betobjectRepository.loadMaxIdByExternalUUIDAndStatus(externalUUID, status);
        if(maxId == null) maxId = 0L;
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_EXTERNALUUID+ externalUUID,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_EXTERNALUUID + externalUUID))
                );
        return betobjectData.isPresent() ? this.toDTO(betobjectData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByExternalUUIDAndStatus(UUID externalUUID) {
        return this.findBetObjectByExternalUUIDAndStatus(externalUUID, GenericStatusEnums.ATIVO.getShortValue());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = betobjectRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_DATECREATED + dateCreated,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_DATECREATED + dateCreated))
                );
        return betobjectData.isPresent() ? this.toDTO(betobjectData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByDateCreatedAndStatus(Date dateCreated) {
        return this.findBetObjectByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = betobjectRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<BetObject> betobjectData =
            Optional.ofNullable( betobjectRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new BetObjectNotFoundException(BET_OBJECT_NOTFOUND_WITH_DATEUPDATED + dateUpdated,
                        HttpStatus.NOT_FOUND,
                            BET_OBJECT_NOTFOUND_WITH_DATEUPDATED + dateUpdated))
                );
        return betobjectData.isPresent() ? this.toDTO(betobjectData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = BetObjectNotFoundException.class
    )
    public BetObjectDTO findBetObjectByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findBetObjectByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetObjectDTO updateWhoById(Long id, String who) {
        findById(id);
        betobjectRepository.updateWhoById(id, who);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetObjectDTO updateExternalUUIDById(Long id, UUID externalUUID) {
        findById(id);
        betobjectRepository.updateExternalUUIDById(id, externalUUID);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetObjectDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        betobjectRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public BetObjectDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        betobjectRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public BetObjectDTO toDTO(BetObject betobject) {
        BetObjectDTO betobjectDTO = new BetObjectDTO();
                betobjectDTO.setId(betobject.getId());
                betobjectDTO.setWho(betobject.getWho());
                betobjectDTO.setExternalUUID(betobject.getExternalUUID());
                betobjectDTO.setJackpot(betobject.getJackpot());
                betobjectDTO.setJackpotPending(betobject.getJackpotPending());
                betobjectDTO.setStatus(betobject.getStatus());
                betobjectDTO.setDateCreated(betobject.getDateCreated());
                betobjectDTO.setDateUpdated(betobject.getDateUpdated());

        return betobjectDTO;
    }

    public BetObject toEntity(BetObjectDTO betobjectDTO) {
        BetObject betobject = null;
        betobject = new BetObject();
                    betobject.setId(betobjectDTO.getId());
                    betobject.setWho(betobjectDTO.getWho());
                    betobject.setExternalUUID(betobjectDTO.getExternalUUID());
                    betobject.setJackpot(betobjectDTO.getJackpot());
                    betobject.setJackpotPending(betobjectDTO.getJackpotPending());
                    betobject.setStatus(betobjectDTO.getStatus());
                    betobject.setDateCreated(betobjectDTO.getDateCreated());
                    betobject.setDateUpdated(betobjectDTO.getDateUpdated());

        return betobject;
    }
}
