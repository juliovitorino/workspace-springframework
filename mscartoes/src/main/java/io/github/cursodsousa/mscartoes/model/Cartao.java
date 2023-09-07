package io.github.cursodsousa.mscartoes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String nome;
    @Column
    @Enumerated(EnumType.STRING)
    private BandeiraCartaoEnum bandeiraCartao;
    @Column
    private Double renda;
    @Column
    private Double limiteCartao;

    public Cartao(String nome,
                  BandeiraCartaoEnum bandeiraCartao,
                  Double renda,
                  Double limiteCartao) {
        this.nome = nome;
        this.bandeiraCartao = bandeiraCartao;
        this.renda = renda;
        this.limiteCartao = limiteCartao;
    }
}
