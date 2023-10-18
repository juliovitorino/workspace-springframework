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

import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.model.UserPunter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.UUID;

/**
* UserPunterService - Interface for UserPunter
*
* @author UserPunter
* @since Sat Oct 14 15:03:48 BRT 2023
*/

public interface UserPunterService extends CommoditieBaseService<UserPunterDTO,UserPunter>
{
    UserPunterDTO findUserPunterByIdAndStatus(Long id);
    UserPunterDTO findUserPunterByIdAndStatus(Long id, String status);
    UserPunterDTO findUserPunterByNicknameAndStatus(String nickname);
    UserPunterDTO findUserPunterByNicknameAndStatus(String nickname, String status);
    UserPunterDTO findUserPunterByBtcAddressAndStatus(String btcAddress);
    UserPunterDTO findUserPunterByBtcAddressAndStatus(String btcAddress, String status);
    UserPunterDTO findUserPunterByDateCreatedAndStatus(Date dateCreated);
    UserPunterDTO findUserPunterByDateCreatedAndStatus(Date dateCreated, String status);
    UserPunterDTO findUserPunterByDateUpdatedAndStatus(Date dateUpdated);
    UserPunterDTO findUserPunterByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<UserPunterDTO> findAllUserPunterByIdAndStatus(Long id, String status);
    List<UserPunterDTO> findAllUserPunterByNicknameAndStatus(String nickname, String status);
    List<UserPunterDTO> findAllUserPunterByBtcAddressAndStatus(String btcAddress, String status);
    List<UserPunterDTO> findAllUserPunterByDateCreatedAndStatus(Date dateCreated, String status);
    List<UserPunterDTO> findAllUserPunterByDateUpdatedAndStatus(Date dateUpdated, String status);

    UserPunterDTO updateNicknameById(Long id, String nickname);
    UserPunterDTO updateBtcAddressById(Long id, String btcAddress);
    UserPunterDTO updateStatusById(Long id, String status);
}
