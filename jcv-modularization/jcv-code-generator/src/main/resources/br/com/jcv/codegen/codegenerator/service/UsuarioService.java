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
* @since Wed Oct 04 13:32:29 BRT 2023
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
