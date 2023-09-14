package io.github.cursodsousa.mscartoes.dto;

import io.github.cursodsousa.mscartoes.model.BandeiraCartaoEnum;
import io.github.cursodsousa.mscartoes.model.Cartao;
import lombok.Data;

@Data
public class CartaoRequest {
    private String nome;
    private BandeiraCartaoEnum bandeira;
    private Double renda;
    private Double limite;

    public Cartao toModel() {
        return new Cartao(this.nome,this.bandeira,this.renda,this.limite);
    }
}
