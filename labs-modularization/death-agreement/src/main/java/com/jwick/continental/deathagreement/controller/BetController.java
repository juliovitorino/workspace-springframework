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


package com.jwick.continental.deathagreement.controller;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import br.com.jcv.commons.library.commodities.dto.RequestFilter;
import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;

import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.constantes.BetConstantes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
* BetController - Controller for Bet API
*
* @author Bet
* @since Fri Oct 06 16:12:54 BRT 2023
*/

@Slf4j
@RestController
@RequestMapping("/v1/api/bet")
public class BetController
{
     @Autowired private BetService betService;

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list")
    public ResponseEntity<List<BetDTO>> findAllBet() {
        try {
            List<BetDTO> bets = betService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());

            if (bets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bets, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list/{status}")
    public ResponseEntity<List<BetDTO>> findAllBet(@PathVariable("status") String status) {
        try {
            List<BetDTO> bets = betService.findAllByStatus(status);

            if (bets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(bets, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/pagefilter")
    public ResponseEntity<Map<String,Object>> pageFilterBetDinamico(@RequestBody RequestFilter filtro) {
        try{
            Map<String,Object> responseFilter = betService.findPageByFilter(filtro);
            return new ResponseEntity<>(responseFilter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBet(@PathVariable("id") long id) {
        try {
            betService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(BetNotFoundException ex) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PostMapping
    public ResponseEntity<Long> createBet(@RequestBody @Valid BetDTO betDTO) {
        try {
            BetDTO betSaved = betService.salvar(betDTO);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(betSaved.getId())
                        .toUri();
                return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<BetDTO> getBetById(@PathVariable("id") Long id) {
      try {
           BetDTO betDTO = betService.findById(id);

           if (betDTO != null) {
               return new ResponseEntity<>(betDTO, HttpStatus.OK);
           } else {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
      } catch (CommoditieBaseException e) {
          return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
          return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<BetDTO> updateBet(@PathVariable("id") Long id, @RequestBody @Valid BetDTO betDTO) {
        BetDTO betData = betService.findById(id);

        if(betData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            betDTO.setId(id);
            betDTO.setDateUpdated(new Date());
            BetDTO betSaved = betService.salvar(betDTO);
            return new ResponseEntity<>(betSaved, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> partialUpdateGeneric(
            @RequestBody Map<String, Object> updates,
            @PathVariable("id") Long id) {
        BetDTO betData = betService.findById(id);
        if (betData == null || !betService.partialUpdate(id, updates)) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok("Bet atualizada");
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<BetDTO> updateStatusById(
            @PathVariable("id") Long id, @PathVariable("status") String status) {
      try {
        BetDTO betUpdated = betService.updateStatusById(id, status);
        return new ResponseEntity<>(betUpdated, HttpStatus.OK);
      } catch(BetNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } catch(CommoditieBaseException e) {
        return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "id")
    public ResponseEntity<BetDTO> findBetById(@RequestParam(BetConstantes.ID) Long id) {
        try{
            BetDTO betDTO = betService.findBetByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "idPunter")
    public ResponseEntity<BetDTO> findBetByIdPunter(@RequestParam(BetConstantes.IDPUNTER) Long idPunter) {
        try{
            BetDTO betDTO = betService.findBetByIdPunterAndStatus(idPunter, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "idBetObject")
    public ResponseEntity<BetDTO> findBetByIdBetObject(@RequestParam(BetConstantes.IDBETOBJECT) Long idBetObject) {
        try{
            BetDTO betDTO = betService.findBetByIdBetObjectAndStatus(idBetObject, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "bet")
    public ResponseEntity<BetDTO> findBetByBet(@RequestParam(BetConstantes.BET) Double bet) {
        try{
            BetDTO betDTO = betService.findBetByBetAndStatus(bet, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "bitcoinAddress")
    public ResponseEntity<BetDTO> findBetByBitcoinAddress(@RequestParam(BetConstantes.BITCOINADDRESS) String bitcoinAddress) {
        try{
            BetDTO betDTO = betService.findBetByBitcoinAddressAndStatus(bitcoinAddress, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "ticket")
    public ResponseEntity<BetDTO> findBetByTicket(@RequestParam(BetConstantes.TICKET) UUID ticket) {
        try{
            BetDTO betDTO = betService.findBetByTicketAndStatus(ticket, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "deathDate")
    public ResponseEntity<BetDTO> findBetByDeathDate(@RequestParam(BetConstantes.DEATHDATE) Date deathDate) {
        try{
            BetDTO betDTO = betService.findBetByDeathDateAndStatus(deathDate, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateCreated")
    public ResponseEntity<BetDTO> findBetByDateCreated(@RequestParam(BetConstantes.DATECREATED) Date dateCreated) {
        try{
            BetDTO betDTO = betService.findBetByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Bet foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateUpdated")
    public ResponseEntity<BetDTO> findBetByDateUpdated(@RequestParam(BetConstantes.DATEUPDATED) Date dateUpdated) {
        try{
            BetDTO betDTO = betService.findBetByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betDTO)
                ? new ResponseEntity<>(betDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
