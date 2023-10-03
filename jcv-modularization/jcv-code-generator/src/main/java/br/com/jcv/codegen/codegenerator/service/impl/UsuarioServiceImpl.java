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


package br.com.jcv.codegen.codegenerator.service.impl;

import java.text.SimpleDateFormat;
import br.com.jcv.codegen.codegenerator.constantes.GenericConstantes;
import br.com.jcv.codegen.codegenerator.dto.MensagemResponse;
import br.com.jcv.codegen.codegenerator.enums.GenericStatusEnums;
import br.com.jcv.codegen.codegenerator.dto.UsuarioDTO;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import br.com.jcv.codegen.codegenerator.constantes.UsuarioConstantes;
import br.com.jcv.codegen.codegenerator.dto.RequestFilter;
import br.com.jcv.codegen.codegenerator.repository.UsuarioRepository;
import br.com.jcv.codegen.codegenerator.service.UsuarioService;
import br.com.jcv.codegen.codegenerator.exception.UsuarioNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;


/**
* UsuarioServiceImpl - Implementation for Usuario interface
*
* @author Usuario
* @since Tue Oct 03 14:14:29 BRT 2023
* @copyright(c), Julio Vitorino
*/


@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService
{
    @Autowired private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UsuarioNotFoundException.class
    )
    public void delete(Long id) {
        log.info("Deletando Usuario com id = {}", id);
        Optional<Usuario> usuarioData =
            Optional.ofNullable(usuarioRepository.findById(id)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com id = " + String.valueOf(id)))
                    );
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        Date now = new Date();
        if(Objects.nonNull(usuarioDTO.getId()) && usuarioDTO.getId() != 0) {
            usuarioDTO.setDateUpdated(now);
        } else {
            usuarioDTO.setStatus(GenericStatusEnums.PENDENTE.getShortValue());
            usuarioDTO.setDateCreated(now);
            usuarioDTO.setDateUpdated(now);
        }
        return this.toDTO(usuarioRepository.save(this.toEntity(usuarioDTO)));
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findById(Long id) {
        Optional<Usuario> usuarioData =
            Optional.ofNullable(usuarioRepository.findById(id)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "Usuario com id = " + String.valueOf(id) + " não encontrado."))
                );

        UsuarioDTO response = this.toDTO(usuarioData.get());
        response.setMensagemResponse(new MensagemResponse("MSG-0001","Comando foi executado com sucesso"));

        return response;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UsuarioNotFoundException.class
    )
    public boolean partialUpdate(Long id, Map<String, Object> updates) {

        Optional<Usuario> usuarioData =
            Optional.ofNullable(usuarioRepository.findById(id)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada " + String.valueOf(id),
                        HttpStatus.NOT_FOUND,
                        "Usuario com id = " + String.valueOf(id) + " não encontrado."))
                    );
        if (usuarioData.isPresent()) {
            Usuario usuario = usuarioData.get();

            for (Map.Entry<String,Object> entry : updates.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.ID)) usuario.setId((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.NOME)) usuario.setNome((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.IDADE)) usuario.setIdade((Long)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.STATUS)) usuario.setStatus((String)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATECREATED)) usuario.setDateCreated((Date)entry.getValue());
                if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATEUPDATED)) usuario.setDateUpdated((Date)entry.getValue());

            if(updates.get(UsuarioConstantes.DATEUPDATED) == null) usuario.setDateUpdated(new Date());
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }
        return false;
    }




    @Override
    @Transactional(transactionManager="aventuratransactionManager",
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class,
        noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO updateStatusById(Long id, String status) {
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrada com id = " + String.valueOf(id),
                    HttpStatus.NOT_FOUND,
                    "Usuario não encontrada com id = " + String.valueOf(id)))
                );
        Usuario usuario = usuarioData.isPresent() ? usuarioData.get() : new Usuario();
        usuario.setStatus(status);
        usuario.setDateUpdated(new Date());
        return toDTO(usuarioRepository.save(usuario));

    }

    @Override
    public List<UsuarioDTO> findAllByStatus(String status) {
        List<UsuarioDTO> lstUsuarioDTO = new ArrayList<>();
        return usuarioRepository.findAllByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

@Override
@Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
)
public Map<String, Object> findPageByFilter(RequestFilter filtro) {
    List<Usuario> lstUsuario = new ArrayList<>();
    Long id = null;
    String nome = null;
    Long idade = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;


    for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.NOME)) nome = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.IDADE)) idade = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

    }

    Pageable paging = PageRequest.of(filtro.getPagina(), filtro.getQtdeRegistrosPorPagina());
    Page<Usuario> paginaUsuario = usuarioRepository.findUsuarioByFilter(
        ,id
        ,nome
        ,idade
        ,status
        ,dateCreated
        ,dateUpdated

        ,paging
    );

    lstUsuario = paginaUsuario.getContent();
    Map<String,Object> response = new HashMap<>();
    response.put("currentPage", paginaUsuario.getNumber());
    response.put("totalItems", paginaUsuario.getTotalElements());
    response.put("totalPages", paginaUsuario.getTotalPages());
    response.put("pageUsuarioItems", lstUsuario.stream().map(m->toDTO(m)).collect(Collectors.toList()));
    return response;
}


    @Override
@Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
)
    public List<UsuarioDTO> findAllByFilter(RequestFilter filtro) {
    Long id = null;
    String nome = null;
    Long idade = null;
    String status = null;
    Date dateCreated = null;
    Date dateUpdated = null;

        for (Map.Entry<String,Object> entry : filtro.getCamposFiltro().entrySet()) {
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.ID)) id = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.NOME)) nome = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.IDADE)) idade = (Long) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.STATUS)) status = (String) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATECREATED)) dateCreated = (Date) entry.getValue() ;
        if(entry.getKey().equalsIgnoreCase(UsuarioConstantes.DATEUPDATED)) dateUpdated = (Date) entry.getValue() ;

        }

        List<Usuario> lstUsuario = usuarioRepository.findUsuarioByFilter(
            ,id
            ,nome
            ,idade
            ,status
            ,dateCreated
            ,dateUpdated

        );

        return lstUsuario.stream().map(m->toDTO(m)).collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public List<UsuarioDTO> findAllUsuarioByIdAndStatus(Long id, String status) {
        return usuarioRepository.findAllByIdAndStatus(id, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public List<UsuarioDTO> findAllUsuarioByNomeAndStatus(String nome, String status) {
        return usuarioRepository.findAllByNomeAndStatus(nome, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public List<UsuarioDTO> findAllUsuarioByIdadeAndStatus(Long idade, String status) {
        return usuarioRepository.findAllByIdadeAndStatus(idade, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public List<UsuarioDTO> findAllUsuarioByDateCreatedAndStatus(Date dateCreated, String status) {
        return usuarioRepository.findAllByDateCreatedAndStatus(dateCreated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public List<UsuarioDTO> findAllUsuarioByDateUpdatedAndStatus(Date dateUpdated, String status) {
        return usuarioRepository.findAllByDateUpdatedAndStatus(dateUpdated, status).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByIdAndStatus(Long id, String status) {
        Long maxId = usuarioRepository.loadMaxIdByIdAndStatus(id, status);
        if(maxId == null) maxId = 0L;
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + id,
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com id = " + id))
                );
        return usuarioData.isPresent() ? this.toDTO(usuarioData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByIdAndStatus(Long id) {
        return this.findUsuarioByIdAndStatus(id, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByNomeAndStatus(String nome, String status) {
        Long maxId = usuarioRepository.loadMaxIdByNomeAndStatus(nome, status);
        if(maxId == null) maxId = 0L;
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + nome,
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com nome = " + nome))
                );
        return usuarioData.isPresent() ? this.toDTO(usuarioData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByNomeAndStatus(String nome) {
        return this.findUsuarioByNomeAndStatus(nome, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByIdadeAndStatus(Long idade, String status) {
        Long maxId = usuarioRepository.loadMaxIdByIdadeAndStatus(idade, status);
        if(maxId == null) maxId = 0L;
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + idade,
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com idade = " + idade))
                );
        return usuarioData.isPresent() ? this.toDTO(usuarioData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByIdadeAndStatus(Long idade) {
        return this.findUsuarioByIdadeAndStatus(idade, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByDateCreatedAndStatus(Date dateCreated, String status) {
        Long maxId = usuarioRepository.loadMaxIdByDateCreatedAndStatus(dateCreated, status);
        if(maxId == null) maxId = 0L;
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + dateCreated,
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com dateCreated = " + dateCreated))
                );
        return usuarioData.isPresent() ? this.toDTO(usuarioData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByDateCreatedAndStatus(Date dateCreated) {
        return this.findUsuarioByDateCreatedAndStatus(dateCreated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByDateUpdatedAndStatus(Date dateUpdated, String status) {
        Long maxId = usuarioRepository.loadMaxIdByDateUpdatedAndStatus(dateUpdated, status);
        if(maxId == null) maxId = 0L;
        Optional<Usuario> usuarioData =
            Optional.ofNullable( usuarioRepository
                .findById(maxId)
                .orElseThrow(
                    () -> new UsuarioNotFoundException("Usuario não encontrada com id = " + dateUpdated,
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrada com dateUpdated = " + dateUpdated))
                );
        return usuarioData.isPresent() ? this.toDTO(usuarioData.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="aventuratransactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = UsuarioNotFoundException.class
    )
    public UsuarioDTO findUsuarioByDateUpdatedAndStatus(Date dateUpdated) {
        return this.findUsuarioByDateUpdatedAndStatus(dateUpdated, GenericStatusEnums.ATIVO.getShortValue());
    }

    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UsuarioDTO updateNomeById(Long id, String nome) {
        findById(id);
        usuarioRepository.updateNomeById(id, nome);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UsuarioDTO updateIdadeById(Long id, Long idade) {
        findById(id);
        usuarioRepository.updateIdadeById(id, idade);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UsuarioDTO updateDateCreatedById(Long id, Date dateCreated) {
        findById(id);
        usuarioRepository.updateDateCreatedById(id, dateCreated);
        return findById(id);
    }
    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public UsuarioDTO updateDateUpdatedById(Long id, Date dateUpdated) {
        findById(id);
        usuarioRepository.updateDateUpdatedById(id, dateUpdated);
        return findById(id);
    }


    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setNome(usuario.getNome());
                usuarioDTO.setIdade(usuario.getIdade());
                usuarioDTO.setStatus(usuario.getStatus());
                usuarioDTO.setDateCreated(usuario.getDateCreated());
                usuarioDTO.setDateUpdated(usuario.getDateUpdated());

        return usuarioDTO;
    }

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = null;
        usuario = new Usuario();
                    usuario.setId(usuarioDTO.getId());
                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setIdade(usuarioDTO.getIdade());
                    usuario.setStatus(usuarioDTO.getStatus());
                    usuario.setDateCreated(usuarioDTO.getDateCreated());
                    usuario.setDateUpdated(usuarioDTO.getDateUpdated());

        return usuario;
    }
}
