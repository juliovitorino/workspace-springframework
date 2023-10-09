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

import com.jwick.continental.deathagreement.dto.JackpotHistoryDTO;
import com.jwick.continental.deathagreement.service.JackpotHistoryService;
import com.jwick.continental.deathagreement.exception.JackpotHistoryNotFoundException;
import com.jwick.continental.deathagreement.constantes.JackpotHistoryConstantes;

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
import javax.validation.Valid;

/**
* JackpotHistoryController - Controller for JackpotHistory API
*
* @author JackpotHistory
* @since Mon Oct 09 08:35:31 BRT 2023
*/

@Slf4j
@RestController
@RequestMapping("/v1/api/jackpothistory")
public class JackpotHistoryController
{
     @Autowired private JackpotHistoryService jackpothistoryService;

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list")
    public ResponseEntity<List<JackpotHistoryDTO>> findAllJackpotHistory() {
        try {
            List<JackpotHistoryDTO> jackpothistorys = jackpothistoryService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());

            if (jackpothistorys.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(jackpothistorys, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list/{status}")
    public ResponseEntity<List<JackpotHistoryDTO>> findAllJackpotHistory(@PathVariable("status") String status) {
        try {
            List<JackpotHistoryDTO> jackpothistorys = jackpothistoryService.findAllByStatus(status);

            if (jackpothistorys.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(jackpothistorys, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/pagefilter")
    public ResponseEntity<Map<String,Object>> pageFilterJackpotHistoryDinamico(@RequestBody RequestFilter filtro) {
        try{
            Map<String,Object> responseFilter = jackpothistoryService.findPageByFilter(filtro);
            return new ResponseEntity<>(responseFilter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteJackpotHistory(@PathVariable("id") long id) {
        try {
            jackpothistoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(JackpotHistoryNotFoundException ex) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PostMapping
    public ResponseEntity<Long> createJackpotHistory(@RequestBody @Valid JackpotHistoryDTO jackpothistoryDTO) {
        try {
            JackpotHistoryDTO jackpothistorySaved = jackpothistoryService.salvar(jackpothistoryDTO);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(jackpothistorySaved.getId())
                        .toUri();
                return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<JackpotHistoryDTO> getJackpotHistoryById(@PathVariable("id") Long id) {
      try {
           JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findById(id);

           if (jackpothistoryDTO != null) {
               return new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK);
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
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<JackpotHistoryDTO> updateJackpotHistory(@PathVariable("id") Long id, @RequestBody @Valid JackpotHistoryDTO jackpothistoryDTO) {
        JackpotHistoryDTO jackpothistoryData = jackpothistoryService.findById(id);

        if(jackpothistoryData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            jackpothistoryDTO.setId(id);
            jackpothistoryDTO.setDateUpdated(new Date());
            JackpotHistoryDTO jackpothistorySaved = jackpothistoryService.salvar(jackpothistoryDTO);
            return new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> partialUpdateGeneric(
            @RequestBody Map<String, Object> updates,
            @PathVariable("id") Long id) {
        JackpotHistoryDTO jackpothistoryData = jackpothistoryService.findById(id);
        if (jackpothistoryData == null || !jackpothistoryService.partialUpdate(id, updates)) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok("JackpotHistory atualizada");
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<JackpotHistoryDTO> updateStatusById(
            @PathVariable("id") Long id, @PathVariable("status") String status) {
      try {
        JackpotHistoryDTO jackpothistoryUpdated = jackpothistoryService.updateStatusById(id, status);
        return new ResponseEntity<>(jackpothistoryUpdated, HttpStatus.OK);
      } catch(JackpotHistoryNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } catch(CommoditieBaseException e) {
        return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "id")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryById(@RequestParam(JackpotHistoryConstantes.ID) Long id) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "description")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByDescription(@RequestParam(JackpotHistoryConstantes.DESCRIPTION) String description) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByDescriptionAndStatus(description, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "type")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByType(@RequestParam(JackpotHistoryConstantes.TYPE) String type) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByTypeAndStatus(type, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "betValue")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByBetValue(@RequestParam(JackpotHistoryConstantes.BETVALUE) String betValue) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByBetValueAndStatus(betValue, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "ticket")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByTicket(@RequestParam(JackpotHistoryConstantes.TICKET) UUID ticket) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByTicketAndStatus(ticket, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "idPunter")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByIdPunter(@RequestParam(JackpotHistoryConstantes.IDPUNTER) Long idPunter) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByIdPunterAndStatus(idPunter, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateCreated")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByDateCreated(@RequestParam(JackpotHistoryConstantes.DATECREATED) Date dateCreated) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo JackpotHistory foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateUpdated")
    public ResponseEntity<JackpotHistoryDTO> findJackpotHistoryByDateUpdated(@RequestParam(JackpotHistoryConstantes.DATEUPDATED) Date dateUpdated) {
        try{
            JackpotHistoryDTO jackpothistoryDTO = jackpothistoryService.findJackpotHistoryByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(jackpothistoryDTO)
                ? new ResponseEntity<>(jackpothistoryDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JackpotHistoryNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
