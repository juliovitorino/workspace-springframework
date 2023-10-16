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
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.model.Bet;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* BetService - Interface for Bet
*
* @author Bet
* @since Fri Oct 06 16:12:54 BRT 2023
*/

public interface BetService extends CommoditieBaseService<BetDTO,Bet>
{
    BetDTO findBetByIdAndStatus(Long id);
    BetDTO findBetByIdAndStatus(Long id, String status);
    BetDTO findBetByIdPunterAndStatus(Long idPunter);
    BetDTO findBetByIdPunterAndStatus(Long idPunter, String status);
    BetDTO findBetByIdBetObjectAndStatus(Long idBetObject);
    BetDTO findBetByIdBetObjectAndStatus(Long idBetObject, String status);
    BetDTO findBetByBetAndStatus(Double bet);
    BetDTO findBetByBetAndStatus(Double bet, String status);
    BetDTO findBetByBitcoinAddressAndStatus(String bitcoinAddress);
    BetDTO findBetByBitcoinAddressAndStatus(String bitcoinAddress, String status);
    BetDTO findBetByTicketAndStatus(UUID ticket);
    BetDTO findBetByTicketAndStatus(UUID ticket, String status);
    BetDTO findBetByDeathDateAndStatus(LocalDate deathDate);
    BetDTO findBetByDeathDateAndStatus(LocalDate deathDate, String status);
    BetDTO findBetByDateCreatedAndStatus(Date dateCreated);
    BetDTO findBetByDateCreatedAndStatus(Date dateCreated, String status);
    BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated);
    BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated, String status);
    BetDTO findBetByIdPunterAndIdBetObjectAndStatus(Long idPunter, Long idBetObject, String status);

    List<BetDTO> findAllBetByIdAndStatus(Long id, String status);
    List<BetDTO> findAllBetByIdPunterAndStatus(Long idPunter, String status);
    List<BetDTO> findAllBetByIdBetObjectAndStatus(Long idBetObject, String status);
    List<BetDTO> findAllBetByBetAndStatus(Double bet, String status);
    List<BetDTO> findAllBetByBitcoinAddressAndStatus(String bitcoinAddress, String status);
    List<BetDTO> findAllBetByTicketAndStatus(UUID ticket, String status);
    List<BetDTO> findAllBetByDeathDateAndStatus(LocalDate deathDate, String status);
    List<BetDTO> findAllBetByDateCreatedAndStatus(Date dateCreated, String status);
    List<BetDTO> findAllBetByDateUpdatedAndStatus(Date dateUpdated, String status);

    BetDTO updateIdPunterById(Long id, Long idPunter);
    BetDTO updateIdBetObjectById(Long id, Long idBetObject);
    BetDTO updateBetById(Long id, Double bet);
    BetDTO updateBitcoinAddressById(Long id, String bitcoinAddress);
    BetDTO updateTicketById(Long id, UUID ticket);
    BetDTO updateDeathDateById(Long id, LocalDate deathDate);
    BetDTO updateStatusById(Long id, String status);
    BetDTO updateDateCreatedById(Long id, Date dateCreated);
    BetDTO updateDateUpdatedById(Long id, Date dateUpdated);


}
