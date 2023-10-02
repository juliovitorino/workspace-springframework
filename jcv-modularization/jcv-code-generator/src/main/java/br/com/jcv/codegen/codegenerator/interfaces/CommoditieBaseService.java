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
* @since Mon Oct 02 19:21:57 BRT 2023
* @copyright(c), Julio Vitorino
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
