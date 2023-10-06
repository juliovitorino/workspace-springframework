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


package com.jwick.continental.deathagreement.service;

import br.com.jcv.commons.library.commodities.service.CommoditieBaseService;
import br.com.jcv.commons.library.commodities.dto.RequestFilter;

import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
* UserService - Interface for User
*
* @author User
* @since Fri Oct 06 15:06:15 BRT 2023
*/

public interface UserService extends CommoditieBaseService<UserDTO,User>
{
    UserDTO findUserByIdAndStatus(Long id);
    UserDTO findUserByIdAndStatus(Long id, String status);
    UserDTO findUserByNicknameAndStatus(String nickname);
    UserDTO findUserByNicknameAndStatus(String nickname, String status);
    UserDTO findUserByBtcAddressAndStatus(String btcAddress);
    UserDTO findUserByBtcAddressAndStatus(String btcAddress, String status);
    UserDTO findUserByDateCreatedAndStatus(Date dateCreated);
    UserDTO findUserByDateCreatedAndStatus(Date dateCreated, String status);
    UserDTO findUserByDateUpdatedAndStatus(Date dateUpdated);
    UserDTO findUserByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<UserDTO> findAllUserByIdAndStatus(Long id, String status);
    List<UserDTO> findAllUserByNicknameAndStatus(String nickname, String status);
    List<UserDTO> findAllUserByBtcAddressAndStatus(String btcAddress, String status);
    List<UserDTO> findAllUserByDateCreatedAndStatus(Date dateCreated, String status);
    List<UserDTO> findAllUserByDateUpdatedAndStatus(Date dateUpdated, String status);

    UserDTO updateNicknameById(Long id, String nickname);
    UserDTO updateBtcAddressById(Long id, String btcAddress);
    UserDTO updateStatusById(Long id, String status);
    UserDTO updateDateCreatedById(Long id, Date dateCreated);
    UserDTO updateDateUpdatedById(Long id, Date dateUpdated);


}
