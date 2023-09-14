package io.github.cursodsousa.msavaliadorcredito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RetornoAvaliacaoCliente {
    List<CartaoAprovado> cartaoAprovadoList;
}
