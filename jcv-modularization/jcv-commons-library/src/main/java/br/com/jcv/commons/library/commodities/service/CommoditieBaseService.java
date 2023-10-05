package br.com.jcv.commons.library.commodities.service;

import br.com.jcv.commons.library.commodities.dto.DTOPadrao;
import br.com.jcv.commons.library.commodities.dto.RequestFilter;

import java.util.List;
import java.util.Map;

/**
* CommoditieBaseService - Interface for CommoditieBaseService
*
* All classes should be implement this class for commodities method
*
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
