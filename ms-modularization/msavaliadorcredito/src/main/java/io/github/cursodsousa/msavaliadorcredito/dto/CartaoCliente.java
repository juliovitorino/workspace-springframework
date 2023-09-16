package io.github.cursodsousa.msavaliadorcredito.dto;

import lombok.Data;

@Data
public class CartaoCliente {
    private String nome;
    private String bandeira;
    private Double limiteLiberado;
}
