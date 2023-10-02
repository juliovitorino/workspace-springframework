package br.com.jcv.codegen.codegenerator.service;

import br.com.jcv.codegen.codegenerator.dto.UsuarioDTO;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import br.com.jcv.codegen.codegenerator.interfaces.CommoditieBaseService;
import br.com.jcv.codegen.codegenerator.dto.RequestFilter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
* UsuarioService - Interface for Usuario
*
* @author Usuario
* @since Mon Oct 02 17:34:33 BRT 2023
* @copyright(c), Julio Vitorino
*/

public interface UsuarioService extends CommoditieBaseService<UsuarioDTO,Usuario>
{
    UsuarioDTO findUsuarioByIdAndStatus(Long id);
    UsuarioDTO findUsuarioByIdAndStatus(Long id, String status);
    UsuarioDTO findUsuarioByNomeAndStatus(String nome);
    UsuarioDTO findUsuarioByNomeAndStatus(String nome, String status);
    UsuarioDTO findUsuarioByIdadeAndStatus(Long idade);
    UsuarioDTO findUsuarioByIdadeAndStatus(Long idade, String status);
    UsuarioDTO findUsuarioByDateCreatedAndStatus(Date dateCreated);
    UsuarioDTO findUsuarioByDateCreatedAndStatus(Date dateCreated, String status);
    UsuarioDTO findUsuarioByDateUpdatedAndStatus(Date dateUpdated);
    UsuarioDTO findUsuarioByDateUpdatedAndStatus(Date dateUpdated, String status);

    List<UsuarioDTO> findAllUsuarioByIdAndStatus(Long id, String status);
    List<UsuarioDTO> findAllUsuarioByNomeAndStatus(String nome, String status);
    List<UsuarioDTO> findAllUsuarioByIdadeAndStatus(Long idade, String status);
    List<UsuarioDTO> findAllUsuarioByDateCreatedAndStatus(Date dateCreated, String status);
    List<UsuarioDTO> findAllUsuarioByDateUpdatedAndStatus(Date dateUpdated, String status);

    UsuarioDTO updateNomeById(Long id, String nome);
    UsuarioDTO updateIdadeById(Long id, Long idade);
    UsuarioDTO updateStatusById(Long id, String status);
    UsuarioDTO updateDateCreatedById(Long id, Date dateCreated);
    UsuarioDTO updateDateUpdatedById(Long id, Date dateUpdated);


}
