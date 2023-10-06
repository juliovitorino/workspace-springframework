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

import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.interfaces.CommoditieBaseService;
import com.jwick.continental.deathagreement.dto.RequestFilter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
* BetService - Interface for Bet
*
* @author Bet
* @since Fri Oct 06 08:29:02 BRT 2023
* @copyright(c), Julio Vitorino
*/

public interface BetService extends CommoditieBaseService<BetDTO,Bet>
{
    BetDTO findBetByIdAndStatus(Long id);
    BetDTO findBetByIdAndStatus(Long id, String status);
    BetDTO findBetByBetAndStatus(Double bet);
    BetDTO findBetByBetAndStatus(Double bet, String status);
    BetDTO findBetByDateCreatedAndStatus(Date dateCreated);
    BetDTO findBetByDateCreatedAndStatus(Date dateCreated, String status);
    BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated);
    BetDTO findBetByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<BetDTO> findAllBetByIdAndStatus(Long id, String status);
    List<BetDTO> findAllBetByBetAndStatus(Double bet, String status);
    List<BetDTO> findAllBetByDateCreatedAndStatus(Date dateCreated, String status);
    List<BetDTO> findAllBetByDateUpdatedAndStatus(Date dateUpdated, String status);

    BetDTO updateBetById(Long id, Double bet);
    BetDTO updateStatusById(Long id, String status);
    BetDTO updateDateCreatedById(Long id, Date dateCreated);
    BetDTO updateDateUpdatedById(Long id, Date dateUpdated);


}
