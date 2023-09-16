package io.github.cursodsousa.msavaliadorcredito.dto;

import io.github.cursodsousa.msavaliadorcredito.dto.CartaoCliente;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosCliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituacaoCliente {
    private DadosCliente dadosCliente;
    private List<CartaoCliente> cartaoClienteList;
}
