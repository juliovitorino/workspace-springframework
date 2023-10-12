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

import br.com.jcv.commons.library.commodities.dto.MensagemResponse;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.commodities.dto.RequestFilter;

import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.model.UserPunter;
import com.jwick.continental.deathagreement.constantes.UserConstantes;
import com.jwick.continental.deathagreement.repository.UserRepository;
import com.jwick.continental.deathagreement.service.UserService;
import com.jwick.continental.deathagreement.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;


/**
* UserServiceImpl - Implementation for User interface
*
* @author User
* @since Fri Oct 06 15:06:15 BRT 2023
*/


@Slf4j
@Service
public class UserServiceImpl implements UserService
{
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando User com id = {}", id);
        Optional<UserPunter> userData =
            Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + id,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com id = " + id))
                    );
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserNotFoundException.class
    )
    public UserDTO salvar(UserDTO userDTO) {
        Date now = new Date();
        if(Objects.nonNull(userDTO.getId()) && userDTO.getId() != 0) {
            userDTO.setDateUpdated(now);
        } else {
            userDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            userDTO.setDateCreated(now);
            userDTO.setDateUpdated(now);
        }
        return this.toDTO(userRepository.save(this.toEntity(userDTO)));
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findById(Long id) {
        Optional<UserPunter> userData =
            Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada " + id,
                    HttpStatus.NOT_FOUND,
                    "User com id = " + id + " não encontrado."))
                );

        UserDTO response = this.toDTO(userData.get());
        response.setMensagemResponse(new MensagemResponse("MSG-0001","Comando foi executado com sucesso"));

        return response;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<UserPunter> userData =
            Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada " + id,
                        HttpStatus.NOT_FOUND,
                        "User com id = " + id + " não encontrado."))
                    );
        if (userData.isPresent()) {
            UserPunter user = userData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(UserConstantes.ID)) user.setId((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserConstantes.NICKNAME)) user.setNickname((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserConstantes.BTCADDRESS)) user.setBtcAddress((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserConstantes.STATUS)) user.setStatus((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserConstantes.DATECREATED)) user.setDateCreated((Date)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UserConstantes.DATEUPDATED)) user.setDateUpdated((Date)entry.getValue());
        }
        if(updates.get(UserConstantes.DATEUPDATED) == null) user.setDateUpdated(new Date());
        userRepository.save(user);
        return true;
    }
        return false;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UserNotFoundException.class
    )
    public UserDTO updateStatusById(Long id, String status) {
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User não encontrada com id = " + id,
                    HttpStatus.NOT_FOUND,
                    "User não encontrada com id = " + id))
                );
        UserPunter user = userData.isPresent() ? userData.get() : new UserPunter();
        user.setStatus(status);
        user.setDateUpdated(new Date());
        return toDTO(userRepository.save(user));

    }

    @Override
    public List<UserDTO> findAllByStatus(String status) {
        List<UserDTO> lstUserDTO = new ArrayList<>();
        return userRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<UserPunter> lstUser = new ArrayList<>();
    Long id = null;
    String nickname = null;
    String btcAddress = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UserConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.NICKNAME)) nickname = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.BTCADDRESS)) btcAddress = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<UserPunter> paginaUser = userRepository.findUserByFilter(paging,
        id
        ,nickname
        ,btcAddress
        ,status
        ,dateCreated
        ,dateUpdated

    );

    lstUser = paginaUser.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaUser.getNumber());
    response.put("totalItems", paginaUser.getTotalElements());
    response.put("totalPages", paginaUser.getTotalPages());
    response.put("pageUserItems", lstUser.stream().map(m->toDTO(m)).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
)
    public List<UserDTO> findAllByFilter(RequestFilter filtro) {
    Long id = null;
    String nickname = null;
    String btcAddress = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UserConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.NICKNAME)) nickname = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.BTCADDRESS)) btcAddress = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UserConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<UserPunter> lstUser = userRepository.findUserByFilter(
            id
            ,nickname
            ,btcAddress
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstUser.stream().map(m->toDTO(m)).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public List<UserDTO> findAllUserByIdAndStatus(Long id, String status) {
        return userRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public List<UserDTO> findAllUserByNicknameAndStatus(String nickname, String status) {
        return userRepository.findAllByNicknameAndStatus(nickname, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public List<UserDTO> findAllUserByBtcAddressAndStatus(String btcAddress, String status) {
        return userRepository.findAllByBtcAddressAndStatus(btcAddress, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public List<UserDTO> findAllUserByDateCreatedAndStatus(Date dateCreated, String status) {
        return userRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public List<UserDTO> findAllUserByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return userRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByIdAndStatus(Long id, String status) {
        Long maxId = userRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + id,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com id = " + id))
                );
        return userData.isPresent() ? this.toDTO(userData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByIdAndStatus(Long id) {
        return this.findUserByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByNicknameAndStatus(String nickname, String status) {
        Long maxId = userRepository.loadMaxIdByNicknameAndStatus(nickname, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + nickname,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com nickname = " + nickname))
                );
        return userData.isPresent() ? this.toDTO(userData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByNicknameAndStatus(String nickname) {
        return this.findUserByNicknameAndStatus(nickname, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByBtcAddressAndStatus(String btcAddress, String status) {
        Long maxId = userRepository.loadMaxIdByBtcAddressAndStatus(btcAddress, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + btcAddress,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com btcAddress = " + btcAddress))
                );
        return userData.isPresent() ? this.toDTO(userData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByBtcAddressAndStatus(String btcAddress) {
        return this.findUserByBtcAddressAndStatus(btcAddress, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = userRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + dateCreated,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com dateCreated = " + dateCreated))
                );
        return userData.isPresent() ? this.toDTO(userData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByDateCreatedAndStatus(Date dateCreated) {
        return this.findUserByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = userRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<UserPunter> userData =
            Optional.ofNullable( userRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UserNotFoundException("User não encontrada com id = " + dateUpdated,
                        HttpStatus.NOT_FOUND,
                        "User não encontrada com dateUpdated = " + dateUpdated))
                );
        return userData.isPresent() ? this.toDTO(userData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UserNotFoundException.class
    )
    public UserDTO findUserByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findUserByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserDTO updateNicknameById(Long id, String nickname) {
        findById(id);
        userRepository.updateNicknameById(id, nickname);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserDTO updateBtcAddressById(Long id, String btcAddress) {
        findById(id);
        userRepository.updateBtcAddressById(id, btcAddress);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        userRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UserDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        userRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public UserDTO toDTO(UserPunter user) {
        UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setNickname(user.getNickname());
                userDTO.setBtcAddress(user.getBtcAddress());
                userDTO.setStatus(user.getStatus());
                userDTO.setDateCreated(user.getDateCreated());
                userDTO.setDateUpdated(user.getDateUpdated());

        return userDTO;
    }

    public UserPunter toEntity(UserDTO userDTO) {
        UserPunter user = null;
        user = new UserPunter();
                    user.setId(userDTO.getId());
                    user.setNickname(userDTO.getNickname());
                    user.setBtcAddress(userDTO.getBtcAddress());
                    user.setStatus(userDTO.getStatus());
                    user.setDateCreated(userDTO.getDateCreated());
                    user.setDateUpdated(userDTO.getDateUpdated());

        return user;
    }
}
