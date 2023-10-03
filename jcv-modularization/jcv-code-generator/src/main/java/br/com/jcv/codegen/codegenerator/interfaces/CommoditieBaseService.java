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


package br.com.jcv.codegen.codegenerator.interfaces;

import br.com.jcv.codegen.codegenerator.dto.RequestFilter;
import br.com.jcv.codegen.codegenerator.dto.DTOPadrao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* CommoditieBaseService - Interface for CommoditieBaseService
*
* All classes should be implement this class for commodities method
*
* @author Usuario
* @since Tue Oct 03 11:43:33 BRT 2023
*/
public interface CommoditieBaseService<DTO extends DTOPadrao, Model> {
  void delete(Long id);
  DTO salvar(DTO dto);
  DTO findById(Long id);
  boolean partialUpdate(Long id, Map<String, Object> updates);
  DTO updateStatusById(Long id, String status);
  List<DTO> findAllByStatus(String status);
  Map<String, Object> findPageByFilter(RequestFilter filtro);
  List<DTO> findAllByFilter(RequestFilter filtro);
  public Model toEntity(DTO dto);
  public DTO toDTO(Model model);
}
