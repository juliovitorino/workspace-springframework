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


package br.com.jcv.codegen.codegenerator.controller;

import br.com.jcv.codegen.codegenerator.dto.UsuarioDTO;
import br.com.jcv.codegen.codegenerator.service.UsuarioService;
import br.com.jcv.codegen.codegenerator.dto.RequestFilter;
import br.com.jcv.codegen.codegenerator.exception.UsuarioNotFoundException;
import br.com.jcv.codegen.codegenerator.exception.CommoditieBaseException;
import br.com.jcv.codegen.codegenerator.enums.GenericStatusEnums;
import br.com.jcv.codegen.codegenerator.constantes.UsuarioConstantes;
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

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
* UsuarioController - Controller for Usuario API
*
* @author Usuario
* @since Tue Oct 03 11:43:33 BRT 2023
* @copyright(c), Julio Vitorino
*/

@Slf4j
@RestController
@RequestMapping("/v1/api/usuario")
public class UsuarioController
{
     @Autowired private UsuarioService usuarioService;

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list")
    public ResponseEntity<List<UsuarioDTO>> findAllUsuario() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue());

            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/list/{status}")
    public ResponseEntity<List<UsuarioDTO>> findAllUsuario(@PathVariable("status") String status) {
        try {
            List<UsuarioDTO> usuarios = usuarioService.findAllByStatus(status);

            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/pagefilter")
    public ResponseEntity<Map<String,Object>> pageFilterUsuarioDinamico(@RequestBody RequestFilter filtro) {
        try{
            Map<String,Object> responseFilter = usuarioService.findPageByFilter(filtro);
            return new ResponseEntity<>(responseFilter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") long id) {
        try {
            usuarioService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(UsuarioNotFoundException ex) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PostMapping
    public ResponseEntity<Long> createUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioSaved = usuarioService.salvar(usuarioDTO);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(usuarioSaved.getId())
                        .toUri();
                return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable("id") Long id) {
      try {
           UsuarioDTO usuarioDTO = usuarioService.findById(id);

           if (usuarioDTO != null) {
               return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
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
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable("id") Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioData = usuarioService.findById(id);

        if(usuarioData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            usuarioDTO.setId(id);
            usuarioDTO.setDateUpdated(new Date());
            UsuarioDTO usuarioSaved = usuarioService.salvar(usuarioDTO);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> partialUpdateGeneric(
            @RequestBody Map<String, Object> updates,
            @PathVariable("id") Long id) {
        UsuarioDTO usuarioData = usuarioService.findById(id);
        if (usuarioData == null || !usuarioService.partialUpdate(id, updates)) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok("Usuario atualizada");
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<UsuarioDTO> updateStatusById(
            @PathVariable("id") Long id, @PathVariable("status") String status) {
      try {
        UsuarioDTO usuarioUpdated = usuarioService.updateStatusById(id, status);
        return new ResponseEntity<>(usuarioUpdated, HttpStatus.OK);
      } catch(UsuarioNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } catch(CommoditieBaseException e) {
        return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "id")
    public ResponseEntity<UsuarioDTO> findUsuarioById(@RequestParam(UsuarioConstantes.ID) Long id) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "nome")
    public ResponseEntity<UsuarioDTO> findUsuarioByNome(@RequestParam(UsuarioConstantes.NOME) String nome) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByNomeAndStatus(nome, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "idade")
    public ResponseEntity<UsuarioDTO> findUsuarioByIdade(@RequestParam(UsuarioConstantes.IDADE) Long idade) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByIdadeAndStatus(idade, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "status")
    public ResponseEntity<UsuarioDTO> findUsuarioByStatus(@RequestParam(UsuarioConstantes.STATUS) String status) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByStatusAndStatus(status, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateCreated")
    public ResponseEntity<UsuarioDTO> findUsuarioByDateCreated(@RequestParam(UsuarioConstantes.DATECREATED) Date dateCreated) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo Usuario foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "dateUpdated")
    public ResponseEntity<UsuarioDTO> findUsuarioByDateUpdated(@RequestParam(UsuarioConstantes.DATEUPDATED) Date dateUpdated) {
        try{
            UsuarioDTO usuarioDTO = usuarioService.findUsuarioByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(usuarioDTO)
                ? new ResponseEntity<>(usuarioDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UsuarioNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
