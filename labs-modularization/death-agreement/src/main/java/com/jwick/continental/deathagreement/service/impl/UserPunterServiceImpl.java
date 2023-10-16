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
import br.com.jcv.commons.library.utility.DateTime;

import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.model.UserPunter;
import com.jwick.continental.deathagreement.constantes.UserPunterConstantes;
import com.jwick.continental.deathagreement.repository.UserPunterRepository;
import com.jwick.continental.deathagreement.service.UserPunterService;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;

import java.text.SimpleDateFormat;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.text.ParseException;
import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;


/**
* UserPunterServiceImpl - Implementation for UserPunter interface
*
* @author UserPunter
* @since Sat Oct 14 15:03:48 BRT 2023
*/


@Slf4j
@Service
public class UserPunterServiceImpl implements UserPunterService
{
    private static final String USERPUNTER_NOTFOUND_WITH_ID = "UserPunter não encontrada com id = ";
    private static final String USERPUNTER_NOTFOUND_WITH_NICKNAME = "UserPunter não encontrada com nickname = ";
    private static final String USERPUNTER_NOTFOUND_WITH_BTCADDRESS = "UserPunter não encontrada com btcAddress = ";
    private static final String USERPUNTER_NOTFOUND_WITH_STATUS = "UserPunter não encontrada com status = ";
    private static final String USERPUNTER_NOTFOUND_WITH_DATECREATED = "UserPunter não encontrada com dateCreated = ";
    private static final String USERPUNTER_NOTFOUND_WITH_DATEUPDATED = "UserPunter não encontrada com dateUpdated = ";


    @Autowired private UserPunterRepository userpunterRepository;
    @Autowired private DateTime dateTime;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserPunterNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando UserPunter com id = {}", id);
        userpunterRepository.findById(id)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID  + id,
                        HttpStatus.NOT_FOUND,
                        USERPUNTER_NOTFOUND_WITH_ID  + id));
        userpunterRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO salvar(UserPunterDTO userpunterDTO) {
        Date now = dateTime.getToday();
        if(Objects.nonNull(userpunterDTO.getId()) && userpunterDTO.getId() != 0) {
            userpunterDTO.setDateUpdated(now);
        } else {
            userpunterDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            userpunterDTO.setDateCreated(now);
            userpunterDTO.setDateUpdated(now);
        }
        return this.toDTO(userpunterRepository.save(this.toEntity(userpunterDTO)));
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findById(Long id) {
        Optional<UserPunter> userpunterData =
            Optional.ofNullable(userpunterRepository.findById(id)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID  + id,
                    HttpStatus.NOT_FOUND,
                    USERPUNTER_NOTFOUND_WITH_ID  + id ))
                );

        return userpunterData.map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserPunterNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<UserPunter> userpunterData =
            Optional.ofNullable(userpunterRepository.findById(id)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID  + id,
                        HttpStatus.NOT_FOUND,
                        USERPUNTER_NOTFOUND_WITH_ID  + id))
                    );
        if (userpunterData.isPresent()) {
            UserPunter userpunter = userpunterData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.NICKNAME)) userpunter.setNickname((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.BTCADDRESS)) userpunter.setBtcAddress((String)entry.getValue());

        }
        if(updates.get(UserPunterConstantes.DATEUPDATED) == null) userpunter.setDateUpdated(new Date());
        userpunterRepository.save(userpunter);
        return true;
    }
        return false;
    }




    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO updateStatusById(Long id, String status) {
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository.findById(id)
                .orElseThrow(() -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID  + id,
                    HttpStatus.NOT_FOUND,
                    USERPUNTER_NOTFOUND_WITH_ID + id))
                );
        UserPunter userpunter = userpunterData.orElseGet(UserPunter::new);
        userpunter.setStatus(status);
        userpunter.setDateUpdated(new Date());
        return toDTO(userpunterRepository.save(userpunter));

    }

    @Override
    public List<UserPunterDTO> findAllByStatus(String status) {
        return userpunterRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<UserPunter> lstUserPunter;
    Long id = null;
    String nickname = null;
    String btcAddress = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.NICKNAME)) nickname = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.BTCADDRESS)) btcAddress = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<UserPunter> paginaUserPunter = userpunterRepository.findUserPunterByFilter(paging,
        id
        ,nickname
        ,btcAddress
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstUserPunter = paginaUserPunter.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaUserPunter.getNumber());
    response.put("totalItems", paginaUserPunter.getTotalElements());
    response.put("totalPages", paginaUserPunter.getTotalPages());
    response.put("pageUserPunterItems", lstUserPunter.stream().map(this::toDTO).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
)
    public List<UserPunterDTO> findAllByFilter(RequestFilter filtro) {
    Long id = null;
    String nickname = null;
    String btcAddress = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.NICKNAME)) nickname = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.BTCADDRESS)) btcAddress = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserPunterConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<UserPunter> lstUserPunter = userpunterRepository.findUserPunterByFilter(
            id
            ,nickname
            ,btcAddress
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstUserPunter.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public List<UserPunterDTO> findAllUserPunterByIdAndStatus(Long id, String status) {
        return userpunterRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public List<UserPunterDTO> findAllUserPunterByNicknameAndStatus(String nickname, String status) {
        return userpunterRepository.findAllByNicknameAndStatus(nickname, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public List<UserPunterDTO> findAllUserPunterByBtcAddressAndStatus(String btcAddress, String status) {
        return userpunterRepository.findAllByBtcAddressAndStatus(btcAddress, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public List<UserPunterDTO> findAllUserPunterByDateCreatedAndStatus(Date dateCreated, String status) {
        return userpunterRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public List<UserPunterDTO> findAllUserPunterByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return userpunterRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByIdAndStatus(Long id, String status) {
        Long maxId = userpunterRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID + id,
                        HttpStatus.NOT_FOUND,
                        USERPUNTER_NOTFOUND_WITH_ID + id))
                );
        return userpunterData.isPresent() ? this.toDTO(userpunterData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByIdAndStatus(Long id) {
        return this.findUserPunterByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByNicknameAndStatus(String nickname, String status) {
        Long maxId = userpunterRepository.loadMaxIdByNicknameAndStatus(nickname, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_NICKNAME + nickname,
                        HttpStatus.NOT_FOUND,
                            USERPUNTER_NOTFOUND_WITH_NICKNAME + nickname))
                );
        return userpunterData.isPresent() ? this.toDTO(userpunterData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByNicknameAndStatus(String nickname) {
        return this.findUserPunterByNicknameAndStatus(nickname, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByBtcAddressAndStatus(String btcAddress, String status) {
        Long maxId = userpunterRepository.loadMaxIdByBtcAddressAndStatus(btcAddress, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_BTCADDRESS + btcAddress,
                        HttpStatus.NOT_FOUND,
                            USERPUNTER_NOTFOUND_WITH_BTCADDRESS + btcAddress))
                );
        return userpunterData.isPresent() ? this.toDTO(userpunterData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByBtcAddressAndStatus(String btcAddress) {
        return this.findUserPunterByBtcAddressAndStatus(btcAddress, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = userpunterRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID + dateCreated,
                        HttpStatus.NOT_FOUND,
                        USERPUNTER_NOTFOUND_WITH_ID + dateCreated))
                );
        return userpunterData.isPresent() ? this.toDTO(userpunterData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByDateCreatedAndStatus(Date dateCreated) {
        return this.findUserPunterByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = userpunterRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userpunterData =
            Optional.ofNullable( userpunterRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID + dateUpdated,
                        HttpStatus.NOT_FOUND,
                        USERPUNTER_NOTFOUND_WITH_ID + dateUpdated))
                );
        return userpunterData.isPresent() ? this.toDTO(userpunterData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserPunterNotFoundException.class
    )
    public UserPunterDTO findUserPunterByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findUserPunterByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserPunterDTO updateNicknameById(Long id, String nickname) {
        findById(id);
        userpunterRepository.updateNicknameById(id, nickname);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserPunterDTO updateBtcAddressById(Long id, String btcAddress) {
        findById(id);
        userpunterRepository.updateBtcAddressById(id, btcAddress);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserPunterDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        userpunterRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserPunterDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        userpunterRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public UserPunterDTO toDTO(UserPunter userpunter) {
        UserPunterDTO userpunterDTO = new UserPunterDTO();
                userpunterDTO.setId(userpunter.getId());
                userpunterDTO.setNickname(userpunter.getNickname());
                userpunterDTO.setBtcAddress(userpunter.getBtcAddress());
                userpunterDTO.setStatus(userpunter.getStatus());
                userpunterDTO.setDateCreated(userpunter.getDateCreated());
                userpunterDTO.setDateUpdated(userpunter.getDateUpdated());

        return userpunterDTO;
    }

    public UserPunter toEntity(UserPunterDTO userpunterDTO) {
        UserPunter userpunter = null;
        userpunter = new UserPunter();
                    userpunter.setId(userpunterDTO.getId());
                    userpunter.setNickname(userpunterDTO.getNickname());
                    userpunter.setBtcAddress(userpunterDTO.getBtcAddress());
                    userpunter.setStatus(userpunterDTO.getStatus());
                    userpunter.setDateCreated(userpunterDTO.getDateCreated());
                    userpunter.setDateUpdated(userpunterDTO.getDateUpdated());

        return userpunter;
    }
}
