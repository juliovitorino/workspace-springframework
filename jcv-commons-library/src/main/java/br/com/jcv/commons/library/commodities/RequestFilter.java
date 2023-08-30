package br.com.jcv.commons.library.commodities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class RequestFilter {
    private int pagina = 0;
    private int qtdeRegistrosPorPagina = 25;
    private boolean ordemAsc = true;
    private String ordemCampo;
    private Map<String, Object> camposFiltro;
}
