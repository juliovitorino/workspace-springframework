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

import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.constantes.BetObjectConstantes;

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
* BetObjectController - Controller for BetObject API
*
* @author BetObject
* @since Fri Oct 06 12:17:45 BRT 2023
*/

@Slf4j
@RestController
@RequestMapping("/v1/api/betobject")
public class BetObjectController
{
     @Autowired private BetObjectService betobjectService;

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list")
    public ResponseEntity<List<BetObjectDTO>> findAllBetObject() {
        try {
            List<BetObjectDTO> betobjects = betobjectService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());

            if (betobjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(betobjects, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list/{status}")
    public ResponseEntity<List<BetObjectDTO>> findAllBetObject(@PathVariable("status") String status) {
        try {
            List<BetObjectDTO> betobjects = betobjectService.findAllByStatus(status);

            if (betobjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(betobjects, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/pagefilter")
    public ResponseEntity<Map<String,Object>> pageFilterBetObjectDinamico(@RequestBody RequestFilter filtro) {
        try{
            Map<String,Object> responseFilter = betobjectService.findPageByFilter(filtro);
            return new ResponseEntity<>(responseFilter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBetObject(@PathVariable("id") long id) {
        try {
            betobjectService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(BetObjectNotFoundException ex) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PostMapping
    public ResponseEntity<Long> createBetObject(@RequestBody @Valid BetObjectDTO betobjectDTO) {
        try {
            BetObjectDTO betobjectSaved = betobjectService.salvar(betobjectDTO);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(betobjectSaved.getId())
                        .toUri();
                return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<BetObjectDTO> getBetObjectById(@PathVariable("id") Long id) {
      try {
           BetObjectDTO betobjectDTO = betobjectService.findById(id);

           if (betobjectDTO != null) {
               return new ResponseEntity<>(betobjectDTO, HttpStatus.OK);
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
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<BetObjectDTO> updateBetObject(@PathVariable("id") Long id, @RequestBody @Valid BetObjectDTO betobjectDTO) {
        BetObjectDTO betobjectData = betobjectService.findById(id);

        if(betobjectData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            betobjectDTO.setId(id);
            betobjectDTO.setDateUpdated(new Date());
            BetObjectDTO betobjectSaved = betobjectService.salvar(betobjectDTO);
            return new ResponseEntity<>(betobjectSaved, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity partialUpdateGeneric(
            @RequestBody Map<String, Object> updates,
            @PathVariable("id") Long id) {
        BetObjectDTO betobjectData = betobjectService.findById(id);
        if (betobjectData == null || !betobjectService.partialUpdate(id, updates)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("BetObject atualizada");
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<BetObjectDTO> updateStatusById(
            @PathVariable("id") Long id, @PathVariable("status") String status) {
      try {
        BetObjectDTO betobjectUpdated = betobjectService.updateStatusById(id, status);
        return new ResponseEntity<>(betobjectUpdated, HttpStatus.OK);
      } catch(BetObjectNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } catch(CommoditieBaseException e) {
        return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "id")
    public ResponseEntity<BetObjectDTO> findBetObjectById(@RequestParam(BetObjectConstantes.ID) Long id) {
        try{
            BetObjectDTO betobjectDTO = betobjectService.findBetObjectByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betobjectDTO)
                ? new ResponseEntity<>(betobjectDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetObjectNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "who")
    public ResponseEntity<BetObjectDTO> findBetObjectByWho(@RequestParam(BetObjectConstantes.WHO) String who) {
        try{
            BetObjectDTO betobjectDTO = betobjectService.findBetObjectByWhoAndStatus(who, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betobjectDTO)
                ? new ResponseEntity<>(betobjectDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetObjectNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "externalUUID")
    public ResponseEntity<BetObjectDTO> findBetObjectByExternalUUID(@RequestParam(BetObjectConstantes.EXTERNALUUID) UUID externalUUID) {
        try{
            BetObjectDTO betobjectDTO = betobjectService.findBetObjectByExternalUUIDAndStatus(externalUUID, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betobjectDTO)
                ? new ResponseEntity<>(betobjectDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetObjectNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateCreated")
    public ResponseEntity<BetObjectDTO> findBetObjectByDateCreated(@RequestParam(BetObjectConstantes.DATECREATED) Date dateCreated) {
        try{
            BetObjectDTO betobjectDTO = betobjectService.findBetObjectByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betobjectDTO)
                ? new ResponseEntity<>(betobjectDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetObjectNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo BetObject foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateUpdated")
    public ResponseEntity<BetObjectDTO> findBetObjectByDateUpdated(@RequestParam(BetObjectConstantes.DATEUPDATED) Date dateUpdated) {
        try{
            BetObjectDTO betobjectDTO = betobjectService.findBetObjectByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(betobjectDTO)
                ? new ResponseEntity<>(betobjectDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BetObjectNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
