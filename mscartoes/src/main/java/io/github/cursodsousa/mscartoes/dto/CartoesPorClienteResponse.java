package io.github.cursodsousa.mscartoes.dto;

import io.github.cursodsousa.mscartoes.model.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartoesPorClienteResponse {
    private String nome;
    private String bandeira;
    private Double limiteLiberado;

    public static CartoesPorClienteResponse fromModel(ClienteCartao clienteCartao) {
        return new CartoesPorClienteResponse(
                clienteCartao.getCartao().getNome(),
                clienteCartao.getCartao().getBandeiraCartao().toString(),
                clienteCartao.getLimite()
        );

    }
}
