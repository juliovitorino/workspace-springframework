package io.github.cursodsousa.msavaliadorcredito.dto;

import lombok.Data;

@Data
public class Cartao {
    private Long id;
    private String nome;
    private String bandeiraCartao;
    private Double limiteCartao;
}
