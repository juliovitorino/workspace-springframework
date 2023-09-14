package io.github.cursodsousa.msavaliadorcredito.dto;

import lombok.Data;

@Data
public class CartaoAprovado {
    private String cartao;
    private String bandeira;
    private Double limiteAprovado;
}
