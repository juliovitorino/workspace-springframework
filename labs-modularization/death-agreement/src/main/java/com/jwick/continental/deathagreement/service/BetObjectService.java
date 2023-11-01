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
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.model.BetObject;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* BetObjectService - Interface for BetObject
*
* @author BetObject
* @since Fri Oct 06 12:17:44 BRT 2023
*/

public interface BetObjectService extends CommoditieBaseService<BetObjectDTO,BetObject>
{
    BetObjectDTO findBetObjectByIdAndStatus(Long id);
    BetObjectDTO findBetObjectByIdAndStatus(Long id, String status);
    BetObjectDTO findBetObjectByWhoAndStatus(String who);
    BetObjectDTO findBetObjectByWhoAndStatus(String who, String status);
    BetObjectDTO findBetObjectByJackpotAndStatus(Double jackpot, String status);
    BetObjectDTO findBetObjectByJackpotPendingAndStatus(Double jackpotPending, String status);
    BetObjectDTO findBetObjectByExternalUUIDAndStatus(UUID externalUUID);
    BetObjectDTO findBetObjectByExternalUUIDAndStatus(UUID externalUUID, String status);
    BetObjectDTO findBetObjectByDateCreatedAndStatus(Date dateCreated);
    BetObjectDTO findBetObjectByDateCreatedAndStatus(Date dateCreated, String status);
    BetObjectDTO findBetObjectByDateUpdatedAndStatus(Date dateUpdated);
    BetObjectDTO findBetObjectByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<BetObjectDTO> findAllBetObjectByIdAndStatus(Long id, String status);
    List<BetObjectDTO> findAllBetObjectByWhoAndStatus(String who, String status);
    List<BetObjectDTO> findAllBetObjectByExternalUUIDAndStatus(UUID externalUUID, String status);
    List<BetObjectDTO> findAllBetObjectByJackpotPendingAndStatus(Double jackpotPending, String status);
    List<BetObjectDTO> findAllBetObjectByJackpotAndStatus(Double jackpot, String status);
    List<BetObjectDTO> findAllBetObjectByDateCreatedAndStatus(Date dateCreated, String status);
    List<BetObjectDTO> findAllBetObjectByDateUpdatedAndStatus(Date dateUpdated, String status);

    BetObjectDTO updateWhoById(Long id, String who);
    BetObjectDTO updateExternalUUIDById(Long id, UUID externalUUID);
    BetObjectDTO updateJackpotById(Long id, Double jackpot);
    BetObjectDTO updateJackpotPendingById(Long id, Double jackpotPending);
    BetObjectDTO updateStatusById(Long id, String status);
}
