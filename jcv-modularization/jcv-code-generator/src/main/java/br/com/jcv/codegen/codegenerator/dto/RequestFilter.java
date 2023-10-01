package br.com.jcv.codegen.codegenerator.dto;

import lombok.*;

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
