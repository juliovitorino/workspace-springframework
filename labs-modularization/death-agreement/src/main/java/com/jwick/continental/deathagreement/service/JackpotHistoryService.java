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

import com.jwick.continental.deathagreement.dto.JackpotHistoryDTO;
import com.jwick.continental.deathagreement.model.JackpotHistory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.UUID;

/**
* JackpotHistoryService - Interface for JackpotHistory
*
* @author JackpotHistory
* @since Mon Oct 09 08:35:31 BRT 2023
*/

public interface JackpotHistoryService extends CommoditieBaseService<JackpotHistoryDTO,JackpotHistory>
{
    JackpotHistoryDTO findJackpotHistoryByIdAndStatus(Long id);
    JackpotHistoryDTO findJackpotHistoryByIdAndStatus(Long id, String status);
    JackpotHistoryDTO findJackpotHistoryByDescriptionAndStatus(String description);
    JackpotHistoryDTO findJackpotHistoryByDescriptionAndStatus(String description, String status);
    JackpotHistoryDTO findJackpotHistoryByTypeAndStatus(String type);
    JackpotHistoryDTO findJackpotHistoryByTypeAndStatus(String type, String status);
    JackpotHistoryDTO findJackpotHistoryByBetValueAndStatus(String betValue);
    JackpotHistoryDTO findJackpotHistoryByBetValueAndStatus(String betValue, String status);
    JackpotHistoryDTO findJackpotHistoryByTicketAndStatus(UUID ticket);
    JackpotHistoryDTO findJackpotHistoryByTicketAndStatus(UUID ticket, String status);
    JackpotHistoryDTO findJackpotHistoryByIdPunterAndStatus(Long idPunter);
    JackpotHistoryDTO findJackpotHistoryByIdPunterAndStatus(Long idPunter, String status);
    JackpotHistoryDTO findJackpotHistoryByDateCreatedAndStatus(Date dateCreated);
    JackpotHistoryDTO findJackpotHistoryByDateCreatedAndStatus(Date dateCreated, String status);
    JackpotHistoryDTO findJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated);
    JackpotHistoryDTO findJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<JackpotHistoryDTO> findAllJackpotHistoryByIdAndStatus(Long id, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByDescriptionAndStatus(String description, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByTypeAndStatus(String type, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByBetValueAndStatus(String betValue, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByTicketAndStatus(UUID ticket, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByIdPunterAndStatus(Long idPunter, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByDateCreatedAndStatus(Date dateCreated, String status);
    List<JackpotHistoryDTO> findAllJackpotHistoryByDateUpdatedAndStatus(Date dateUpdated, String status);

    JackpotHistoryDTO updateDescriptionById(Long id, String description);
    JackpotHistoryDTO updateTypeById(Long id, String type);
    JackpotHistoryDTO updateBetValueById(Long id, String betValue);
    JackpotHistoryDTO updateTicketById(Long id, UUID ticket);
    JackpotHistoryDTO updateIdPunterById(Long id, Long idPunter);
    JackpotHistoryDTO updateStatusById(Long id, String status);
    JackpotHistoryDTO updateDateCreatedById(Long id, Date dateCreated);
    JackpotHistoryDTO updateDateUpdatedById(Long id, Date dateUpdated);


}
