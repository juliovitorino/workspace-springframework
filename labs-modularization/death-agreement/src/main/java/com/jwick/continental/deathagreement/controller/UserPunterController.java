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

import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.service.UserPunterService;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;
import com.jwick.continental.deathagreement.constantes.UserPunterConstantes;

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
* UserPunterController - Controller for UserPunter API
*
* @author UserPunter
* @since Sat Oct 14 15:03:48 BRT 2023
*/

@Slf4j
@RestController
@RequestMapping("/v1/api/userpunter")
public class UserPunterController
{
     @Autowired private UserPunterService userpunterService;

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list")
    public ResponseEntity<List<UserPunterDTO>> findAllUserPunter() {
        try {
            List<UserPunterDTO> userpunters = userpunterService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());

            if (userpunters.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userpunters, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list/{status}")
    public ResponseEntity<List<UserPunterDTO>> findAllUserPunter(@PathVariable("status") String status) {
        try {
            List<UserPunterDTO> userpunters = userpunterService.findAllByStatus(status);

            if (userpunters.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userpunters, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/pagefilter")
    public ResponseEntity<Map<String,Object>> pageFilterUserPunterDinamico(@RequestBody RequestFilter filtro) {
        try{
            Map<String,Object> responseFilter = userpunterService.findPageByFilter(filtro);
            return new ResponseEntity<>(responseFilter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserPunter(@PathVariable("id") long id) {
        try {
            userpunterService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(UserPunterNotFoundException ex) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PostMapping
    public ResponseEntity<Long> createUserPunter(@RequestBody @Valid UserPunterDTO userpunterDTO) {
        try {
            UserPunterDTO userpunterSaved = userpunterService.salvar(userpunterDTO);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userpunterSaved.getId())
                        .toUri();
                return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<UserPunterDTO> getUserPunterById(@PathVariable("id") Long id) {
      try {
           UserPunterDTO userpunterDTO = userpunterService.findById(id);

           if (userpunterDTO != null) {
               return new ResponseEntity<>(userpunterDTO, HttpStatus.OK);
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
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<UserPunterDTO> updateUserPunter(@PathVariable("id") Long id, @RequestBody @Valid UserPunterDTO userpunterDTO) {
        UserPunterDTO userpunterData = userpunterService.findById(id);

        if(userpunterData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            userpunterDTO.setId(id);
            userpunterDTO.setDateUpdated(new Date());
            UserPunterDTO userpunterSaved = userpunterService.salvar(userpunterDTO);
            return new ResponseEntity<>(userpunterSaved, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity partialUpdateGeneric(
            @RequestBody Map<String, Object> updates,
            @PathVariable("id") Long id) {
        UserPunterDTO userpunterData = userpunterService.findById(id);
        if (userpunterData == null || !userpunterService.partialUpdate(id, updates)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("UserPunter atualizada");
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<UserPunterDTO> updateStatusById(
            @PathVariable("id") Long id, @PathVariable("status") String status) {
      try {
        UserPunterDTO userpunterUpdated = userpunterService.updateStatusById(id, status);
        return new ResponseEntity<>(userpunterUpdated, HttpStatus.OK);
      } catch(UserPunterNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } catch(CommoditieBaseException e) {
        return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "id")
    public ResponseEntity<UserPunterDTO> findUserPunterById(@RequestParam(UserPunterConstantes.ID) Long id) {
        try{
            UserPunterDTO userpunterDTO = userpunterService.findUserPunterByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(userpunterDTO)
                ? new ResponseEntity<>(userpunterDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserPunterNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "nickname")
    public ResponseEntity<UserPunterDTO> findUserPunterByNickname(@RequestParam(UserPunterConstantes.NICKNAME) String nickname) {
        try{
            UserPunterDTO userpunterDTO = userpunterService.findUserPunterByNicknameAndStatus(nickname, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(userpunterDTO)
                ? new ResponseEntity<>(userpunterDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserPunterNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "btcAddress")
    public ResponseEntity<UserPunterDTO> findUserPunterByBtcAddress(@RequestParam(UserPunterConstantes.BTCADDRESS) String btcAddress) {
        try{
            UserPunterDTO userpunterDTO = userpunterService.findUserPunterByBtcAddressAndStatus(btcAddress, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(userpunterDTO)
                ? new ResponseEntity<>(userpunterDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserPunterNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateCreated")
    public ResponseEntity<UserPunterDTO> findUserPunterByDateCreated(@RequestParam(UserPunterConstantes.DATECREATED) Date dateCreated) {
        try{
            UserPunterDTO userpunterDTO = userpunterService.findUserPunterByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(userpunterDTO)
                ? new ResponseEntity<>(userpunterDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserPunterNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo UserPunter foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateUpdated")
    public ResponseEntity<UserPunterDTO> findUserPunterByDateUpdated(@RequestParam(UserPunterConstantes.DATEUPDATED) Date dateUpdated) {
        try{
            UserPunterDTO userpunterDTO = userpunterService.findUserPunterByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(userpunterDTO)
                ? new ResponseEntity<>(userpunterDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UserPunterNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
